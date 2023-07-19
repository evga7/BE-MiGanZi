package com.StreetNo5.StreetNo5.domain.dto;


import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.eums.Role;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SignupForm {

    private String nickname;
    private String password;
    public User toEntity(String encPwd) {
        return User.builder()
                .nickname(nickname)
                .password(encPwd)
                .role(Role.USER)
                .build();
    }
}
