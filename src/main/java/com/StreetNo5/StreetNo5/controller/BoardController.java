package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserPostService userPostService;
    @GetMapping("/")
    public Page<UserPost> getBoardList(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.ASC)Pageable pageable) {
        Page<UserPost> userPosts = userPostService.getUserPosts(pageable);
        return userPosts;
    }
    @GetMapping("/{id}")
    public UserPost getPost(@PathVariable Long id) {
        userPostService.updateView(id);
        return userPostService.getUserPost(id);
    }
    @PostMapping("/post")
    public String writePost(UserPost userPost){
        UserPost post = UserPost.builder()
                .title(userPost.getTitle())
                .nickname(userPost.getNickname())
                .content(userPost.getContent())
                .build();
        userPostService.writePost(post);
        return "OK";

    }




}

