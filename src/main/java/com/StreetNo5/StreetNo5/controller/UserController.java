package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.dtos.SignupForm;
import com.StreetNo5.StreetNo5.service.UserService;
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

    @PostMapping("/login")
    public ResponseEntity<String> loginSuccess(String nickname) {
        String token = userService.login(nickname);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public Long signup(SignupForm signupForm) {
        return userService.signup(signupForm);
    }

    @GetMapping("/check/{nickname}")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNickNameExists(nickname));
    }



}
