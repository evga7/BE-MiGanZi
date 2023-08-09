package com.StreetNo5.StreetNo5.service;


import com.StreetNo5.StreetNo5.config.jwt.JwtTokenProvider;
import com.StreetNo5.StreetNo5.config.redis.RefreshToken;
import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.domain.dto.ApiResponse;
import com.StreetNo5.StreetNo5.domain.dto.SignupForm;
import com.StreetNo5.StreetNo5.domain.dto.UserResponseDto;
import com.StreetNo5.StreetNo5.lib.Helper;
import com.StreetNo5.StreetNo5.repository.UserRepository;
import com.StreetNo5.StreetNo5.service.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApiResponse response;
    private final RedisService redisService;
    private final RedisTemplate redisTemplate;



    public List<User> findAlluser(){return userRepository.findAll();}
    public Optional<User> findUser(String Nickname){
        return userRepository.findByNickname(Nickname);
    }
    public ResponseEntity<?> login(HttpServletRequest request,String nickname, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickname , password);

        // 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


        // 검증된 인증 정보로 JWT 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        redisService.saveRefreshToken(RefreshToken.builder()
                .id(authentication.getName())
                .ip(Helper.getClientIp(request))
                .authorities(authentication.getAuthorities())
                .refreshToken(tokenInfo.getRefreshToken())
                .build());
        return response.success(tokenInfo);
    }

    public ResponseEntity<?> logout(String token) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // 3. Redis 에서 해당 User name 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        Optional<RefreshToken> byId = redisService.findRefreshTokenById((authentication.getName()));
        if (byId.get() != null) {
            // Refresh Token 삭제
            redisService.removeRefreshToken(byId.get());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(token);
        redisTemplate.opsForValue()
                .set(token, "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<?> withdrawal(String token,String nickname,String checkNickname) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(token) || !nickname.equals(checkNickname)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // 3. Redis 에서 해당 User name 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        Optional<RefreshToken> byId = redisService.findRefreshTokenById((authentication.getName()));
        if (byId.get() != null) {
            // Refresh Token 삭제
            redisService.removeRefreshToken(byId.get());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(token);
        redisTemplate.opsForValue()
                .set(token, "logout", expiration, TimeUnit.MILLISECONDS);

        Optional<User> byNickname = userRepository.findByNickname(nickname);
        if (!byNickname.isEmpty()){
            userRepository.delete(byNickname.get());
        }
        else{
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        return response.success("회원탈퇴 되었습니다.",HttpStatus.ACCEPTED);
    }

    public Long signup(SignupForm signupForm) {
        boolean check = checkNickNameExists(signupForm.getNickname());

        if (check) {
            return -1L;
        }

        String encPwd = encoder.encode(signupForm.getPassword());

        User user = userRepository.save(signupForm.toEntity(encPwd));

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
                RefreshToken refreshToken = redisService.findByRefreshString(token);
                if (refreshToken != null) {
                    //4. 최초 로그인한 ip 와 같은지 확인 (처리 방식에 따라 재발급을 하지 않거나 메일 등의 알림을 주는 방법이 있음)
                    String currentIpAddress = Helper.getClientIp(request);
                    if (refreshToken.getIp().equals(currentIpAddress)) {
                        // 5. Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
                        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateAccessToken(refreshToken.getId(), refreshToken.getAuthorities());

/*                        // 4. Redis RefreshToken update
                        refreshTokenRedisRepository.save(RefreshToken.builder()
                                .id(refreshToken.getId())
                                .ip(currentIpAddress)
                                .authorities(refreshToken.getAuthorities())
                                .refreshToken(tokenInfo.getRefreshToken())
                                .build());*/

                        return response.success(tokenInfo);
                    }
                    else
                    {
                        return response.fail("새로운곳에서 로그인하여 실패했습니다.",HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        return response.fail("토큰 갱신에 실패했습니다.",HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<?> changePassword(String token,String newPassword){
        if (jwtTokenProvider.validateToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            String encPwd = encoder.encode(newPassword);

            Optional<User> user = userRepository.findByNickname(authentication.getName());
            if (!user.isPresent()){
                return response.fail("유저가 존재하지 않습니다.",HttpStatus.BAD_REQUEST);
            }
            user.get().passwordUpdate(encPwd);
            return response.success("비밀번호가 변경 되었습니다.",HttpStatus.ACCEPTED);
        }
        return response.fail("비밀번호 변경에 실패했습니다.",HttpStatus.BAD_REQUEST);
    }


    public List<UserPost> getUserPosts(String nickname){
        Optional<User> byNickname = userRepository.findByNickname(nickname);
        return byNickname.get().getUserPosts();
    }
    public List<UserComment> getUserComments(String nickname){
        Optional<User> byNickname = userRepository.findByNickname(nickname);
        return byNickname.get().getUserComments();
    }
    public ResponseEntity<?> changeNickName(String token,String userNickname,String newNickname)
    {
        if (!jwtTokenProvider.validateToken(token)){
            return response.fail("토큰이 유효하지 않습니다.",HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findByNickname(userNickname);
        if (!user.isPresent()){
            return response.fail("유저가 존재하지 않습니다.",HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByNickname(newNickname).isPresent()){
            return response.fail("유저 닉네임 설정 오류",HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, newNickname);
        user.get().nicknameUpdate(newNickname);
        logout(token);
        // 기존 토큰을 또 사용하다 보니 버그 발견
        return response.success("닉네임 설정 변경 성공",tokenInfo);

    }

    public boolean checkNickNameExists(String nickname) {
       return userRepository.existsUsersByNickname(nickname);
    }

}
