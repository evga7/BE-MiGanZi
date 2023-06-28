package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.CommentService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user/board")
@RestController

public class CommentController {
    private final CommentService commentService;
    private final UserPostService userPostService;

    @PostMapping("/comment")
    public String write_comment(UserComment userComment,Long post_id){
        UserPost userPost = userPostService.getUserPost(post_id);
        userPost.addComment(userComment);
        userPostService.updateCommentCount(post_id);
        commentService.write_comment(userComment);
        return "OK";
    }

}
