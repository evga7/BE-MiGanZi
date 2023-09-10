package com.StreetNo5.StreetNo5.entity.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePassword {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 , 숫자를 1개 이상 포함해야 합니다.")
    private String newPassword;
}
