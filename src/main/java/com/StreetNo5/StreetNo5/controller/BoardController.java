package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.service.GCSService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserPostService userPostService;
    private final GCSService gcsService;

    @Operation(summary = "전체 게시물 조회 API")
    @GetMapping("/posts")
    public Slice<UserPost> getBoardList(@PageableDefault(size = 6,sort = "createdDate",direction = Sort.Direction.DESC)Pageable pageable) {
        Slice<UserPost> userPosts = userPostService.getUserPosts(pageable);
        return userPosts;
    }

    @Operation(summary = "인기 게시물(조회수 기준) 5개 조회 API")
    @GetMapping("/popular-post")
    public List<UserPost> getPopularPost()
    {
        return userPostService.getUserPolarPost();
    }
    @Operation(summary = "게시물 번호를 이용한 게시물 조회 API")
    @GetMapping("/{id}")
    public UserPost getPost(@PathVariable Long id) {
        userPostService.updateView(id);
        return userPostService.getUserPost(id);
    }

    @Operation(summary = "게시글 작성 API")
    @PostMapping("/post")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String writePost(UserPost userPost, @RequestHeader(value = "Authorization") String token, MultipartFile imageFile) throws IOException {

        String nickname = getUserNicknameFromJwtToken(token);
        String imageUrl = gcsService.updateMemberInfo(imageFile);
        UserPost post = UserPost.builder()
                .nickname(nickname)
                .content(userPost.getContent())
                .imageUrl(imageUrl)
                .lat(userPost.getLat())
                .lng(userPost.getLng())
                .tags(userPost.getTags())
                .tags_num(convertTags(userPost.getTags()))
                .music_id(userPost.getMusic_id())
                .build();
        userPostService.writePost(post);
        return "OK";

    }

    private String getUserNicknameFromJwtToken(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitJwt = token.split("\\.");
        final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
        String nickname = payloadStr.split(":")[1].replace("\"", "").split(",")[0];
        return nickname;
    }
    private Long convertTags(String tags){
        return Long.parseLong(tags,2);
    }


}

