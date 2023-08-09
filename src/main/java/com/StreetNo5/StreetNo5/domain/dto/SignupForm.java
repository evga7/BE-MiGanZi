package com.StreetNo5.StreetNo5.domain.dto;


import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.eums.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SignupForm {

    @NotNull
    private String nickname;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 , 숫자를 1개 이상 포함해야 합니다.")
    private String password;
    public User toEntity(String encPwd) {
        return User.builder()
                .nickname(nickname)
                .password(encPwd)
                .role(Role.USER)
                .build();
    }
}
