package com.StreetNo5.StreetNo5.service;


import com.StreetNo5.StreetNo5.config.jwt.JwtTokenProvider;
import com.StreetNo5.StreetNo5.config.redis.RefreshToken;
import com.StreetNo5.StreetNo5.domain.Users;
import com.StreetNo5.StreetNo5.domain.dto.ApiResponse;
import com.StreetNo5.StreetNo5.domain.dto.SignupForm;
import com.StreetNo5.StreetNo5.domain.dto.UserResponseDto;
import com.StreetNo5.StreetNo5.lib.Helper;
import com.StreetNo5.StreetNo5.repository.RefreshTokenRedisRepository;
import com.StreetNo5.StreetNo5.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository repository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final ApiResponse response;


    public ResponseEntity<?> login(HttpServletRequest request,String nickname, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickname , password);

        // 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


        // 검증된 인증 정보로 JWT 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        RefreshToken build = RefreshToken.builder()
                .id(authentication.getName())
                .ip(Helper.getClientIp(request))
                .authorities(authentication.getAuthorities())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
        refreshTokenRedisRepository.save(build);

        return response.success(tokenInfo);
    }

    public Long signup(SignupForm signupForm) {
        boolean check = checkNickNameExists(signupForm.getNickname());

        if (check) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        String encPwd = encoder.encode(signupForm.getPassword());

        Users user = repository.save(signupForm.toEntity(encPwd));

        if(user!=null) {
            return user.getId();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> reissue(HttpServletRequest request) {
        //TODO:: 1, 2 는 JwtAuthenticationFilter 동작과 중복되는 부분, 때문에 jwt filter 에서 다른 key 값으로 refresh token 값을
        //넘겨주고 여기서 받아서 처리하는 방법도 적용해 볼 수 있을 듯

        //1. Request Header 에서 JWT Token 추출
        String token = jwtTokenProvider.resolveToken(request);

        //2. validateToken 메서드로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //3. refresh token 인지 확인
            if (jwtTokenProvider.isRefreshToken(token)) {
                //refresh token
                RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
                if (refreshToken != null) {
                    //4. 최초 로그인한 ip 와 같은지 확인 (처리 방식에 따라 재발급을 하지 않거나 메일 등의 알림을 주는 방법이 있음)
                    String currentIpAddress = Helper.getClientIp(request);
                    if (refreshToken.getIp().equals(currentIpAddress)) {
                        // 5. Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
                        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(refreshToken.getId(), refreshToken.getAuthorities());

                        // 4. Redis RefreshToken update
                        refreshTokenRedisRepository.save(RefreshToken.builder()
                                .id(refreshToken.getId())
                                .ip(currentIpAddress)
                                .authorities(refreshToken.getAuthorities())
                                .refreshToken(tokenInfo.getRefreshToken())
                                .build());

                        return response.success(tokenInfo);
                    }
                }
            }
        }

        return response.fail("토큰 갱신에 실패했습니다.");
    }

    public boolean checkNickNameExists(String nickname) {
       return repository.existsUsersByNickname(nickname);
    }

}
