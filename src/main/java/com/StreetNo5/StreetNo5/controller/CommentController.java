package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.CommentService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/comment")
    public List<UserComment> write_comment(UserComment userComment,Long post_id,@RequestHeader(value = "Authorization") String token){
        UserPost userPost = userPostService.getUserPost(post_id);
        userComment.setNickname(getUserNicknameFromJwtToken(token));
        userPost.addComment(userComment);
        List<UserComment> userComments = userPost.getUserComments();
        userPostService.updateCommentCount(post_id);
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
