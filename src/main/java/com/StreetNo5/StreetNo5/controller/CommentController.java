package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.CommentService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user/board")
@RestController

public class CommentController {
    private final CommentService commentService;
    private final UserPostService userPostService;

    @Operation(summary = "댓글 작성 API")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/comment")
    public List<UserComment> write_comment(Long postId, String content, @RequestHeader(value = "Authorization") String token){
        UserComment userComment = new UserComment();
        userComment.setContent(content);
        UserPost userPost = userPostService.getUserPost(postId);
        userComment.setNickname(getUserNicknameFromJwtToken(token));
        userPost.addComment(userComment);
        List<UserComment> userComments = userPost.getUserComments();
        userPostService.updateCommentCount(postId);
        commentService.write_comment(userComment);
        return userComments;
    }
    private String getUserNicknameFromJwtToken(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitJwt = token.split("\\.");
        final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
        String nickname = payloadStr.split(":")[1].replace("\"", "").split(",")[0];
        return nickname;
    }

}
