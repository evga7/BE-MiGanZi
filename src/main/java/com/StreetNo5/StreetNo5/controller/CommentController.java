package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.repository.BoardRepository;
import com.StreetNo5.StreetNo5.service.CommentService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(originPatterns = "https://miganzi.vercel.app/")
public class CommentController {
    private final CommentService commentService;
    private final UserPostService userPostService;
    @PostMapping("/board/comment")
    @CrossOrigin(originPatterns = "http://localhost:3000")
    public String write_comment(UserComment userComment,Long id){
        UserPost userPost = userPostService.getUserPost(id);
        userPost.addComment(userComment);
        userPostService.updateCommentCount(id);
        commentService.write_comment(userComment);
        return "OK";
    }

}
