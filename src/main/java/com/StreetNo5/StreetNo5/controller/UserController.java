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

    @Operation(summary = "닉네임 조건 확인 API")
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
