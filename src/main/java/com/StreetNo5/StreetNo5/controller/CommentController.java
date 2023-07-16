package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.CommentService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import com.StreetNo5.StreetNo5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/user/board")
@RestController

public class CommentController {
    private final CommentService commentService;
    private final UserPostService userPostService;
    private final UserService userService;

    @Operation(summary = "댓글 작성 API")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/comment")
    public List<UserComment> write_comment(Long postId, String content, @RequestHeader(value = "Authorization") String token){
        UserComment userComment = new UserComment();
        userComment.setContent(content);
        UserPost userPost = userPostService.getUserPost(postId);
        String nickname = getUserNicknameFromJwtToken(token);
        userComment.setNickname(nickname);
        userPost.addComment(userComment);

        Optional<User> user = userService.findUser(nickname);
        User user1 = user.get();
        user1.addComment(userComment);
        userComment.setUser(user1);
        userPostService.updateCommentCount(postId);
        commentService.write_comment(userComment);
        List<UserComment> userComments = userPost.getUserComments();
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
