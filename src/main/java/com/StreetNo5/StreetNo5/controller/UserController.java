package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.dtos.SignupForm;
import com.StreetNo5.StreetNo5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<String> loginSuccess(String nickname,String password) {
        String token = userService.login(nickname,password);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public Long signup(SignupForm signupForm) {
        return userService.signup(signupForm);
    }

    @Operation(summary = "닉네임 조건 확인 API",description = "반환값 String, 닉네임 존재시 extis," +
            "빈값 empty," +
            "8글자 이상 length," +
            "특수문자나 닉네임 원칙 안맞으면 notMatch , " +
            "정상적일시 OK 반환 입니다 그냥 OK 이외에는 다 안되는 값이라고 판한해서 값을 return 합니다.")
    @GetMapping("/check/{nickname}")
    public String checkNickNameDuplicate(@PathVariable String nickname) {
        boolean matches = nickname.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
        if (userService.checkNickNameExists(nickname))
            return "exits";
        else if (nickname.contains("운영자")||nickname.contains("admin"))
            return "noNickName";
        else if (nickname.isEmpty())
            return "empty";
        else if (nickname.length()>8)
            return "length";
        else if (!matches)
            return "notMatch";
        else
            return "OK";
    }




}
