package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "전체 게시물 조회 API")
    @GetMapping("/")
    public Page<UserPost> getBoardList(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.ASC)Pageable pageable) {
        Page<UserPost> userPosts = userPostService.getUserPosts(pageable);
        return userPosts;
    }
    @Operation(summary = "게시물 번호를 이용한 게시물 조회 API")
    @GetMapping("/{id}")
    public UserPost getPost(@PathVariable Long id) {
        userPostService.updateView(id);
        return userPostService.getUserPost(id);
    }

    @Operation(summary = "게시글 작성 API")
    @PostMapping("/post")
    public String writePost(UserPost userPost){
        UserPost post = UserPost.builder()
                .title(userPost.getTitle())
                .nickname(userPost.getNickname())
                .content(userPost.getContent())
                .lat(userPost.getLat())
                .lng(userPost.getLng())
                .tags(userPost.getTags())
                .build();
        userPostService.writePost(post);
        return "OK";

    }




}

