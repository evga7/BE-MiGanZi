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

import java.util.Base64;

@RestController
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserPostService userPostService;

    @Operation(summary = "전체 게시물 조회 API")
    @GetMapping("/posts")
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
    public String writePost(UserPost userPost,@RequestHeader(value = "Authorization") String token){

        Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitJwt = token.split("\\.");
        final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
        String nickname = payloadStr.split(":")[1].replace("\"", "").split(",")[0];

        UserPost post = UserPost.builder()
                .title(userPost.getTitle())
                .nickname(nickname)
                .content(userPost.getContent())
                .lat(userPost.getLat())
                .lng(userPost.getLng())
                .tags(userPost.getTags())
                .music_id(userPost.getMusic_id())
                .build();
        userPostService.writePost(post);
        return "OK";

    }




}

