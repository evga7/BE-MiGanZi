package com.StreetNo5.StreetNo5.service;


import com.StreetNo5.StreetNo5.config.auth.UserDetailsImpl;
import com.StreetNo5.StreetNo5.config.auth.UserDetailsServiceImpl;
import com.StreetNo5.StreetNo5.config.jwt.JwtTokenProvider;
import com.StreetNo5.StreetNo5.domain.Users;
import com.StreetNo5.StreetNo5.domain.dtos.SignupForm;
import com.StreetNo5.StreetNo5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${temp.password}")
    private String tempPassWord;


    public String login(String nickname) {
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(nickname);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickname , "1234",userDetails.getAuthorities());

        // 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 검증된 인증 정보로 JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    public Long signup(SignupForm signupForm) {
        boolean check = checkNickNameExists(signupForm.getNickname());

        if (check) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        String encPwd = encoder.encode(tempPassWord);

        Users user = repository.save(signupForm.toEntity(encPwd));

        if(user!=null) {
            return user.getId();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public boolean checkNickNameExists(String nickname) {
       return repository.existsUsersByNickname(nickname);
    }

}
