package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.config.redis.UserAlert;
import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.CommentService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import com.StreetNo5.StreetNo5.service.UserService;
import com.StreetNo5.StreetNo5.service.redis.RedisService;
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
    private final PubSubController pubSubController;
    private final RedisService redisService;

    @Operation(summary = "댓글 작성 API")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/comment/write")
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
        // 다른사람의 댓글만 알림
        if (!userPost.getUser().getNickname().equals(userComment.getNickname())) {
            pubSubController.pushMessage(userPost.getUser().getNickname(), "댓글이 등록되었습니다.", userComment.getCreatedDate(), userComment.getContent());
            redisService.saveUserAlert(UserAlert.builder()
                    .createdDate(userComment.getCreatedDate())
                    .title("댓글이 등록되었습니다.")
                    .nickname(userPost.getUser().getNickname())
                    .message(userComment.getContent())
                    .build());
        }
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
