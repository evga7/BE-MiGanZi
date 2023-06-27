package com.StreetNo5.StreetNo5.domain.dtos;


import com.StreetNo5.StreetNo5.domain.Users;
import com.StreetNo5.StreetNo5.domain.eums.Role;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SignupForm {

    private String nickname;
    private String password;
    public Users toEntity(String encPwd) {
        return Users.builder()
                .nickname(nickname)
                .password(encPwd)
                .role(Role.USER)
                .build();
    }
}
