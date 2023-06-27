package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {



    private final UserPostService userPostService;
    @GetMapping("/board")
    public Slice<UserPost> getBoardList(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.ASC)Pageable pageable) {
        Slice<UserPost> userPosts = userPostService.getUserPosts(pageable);
        return userPosts;
    }
    @GetMapping("/board/{id}")
    public UserPost getPost(@PathVariable Long id) {
        userPostService.updateView(id);
        return userPostService.getUserPost(id);
    }



}
