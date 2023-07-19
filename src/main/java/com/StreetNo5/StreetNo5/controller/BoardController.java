package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.domain.dto.UserPostDto;
import com.StreetNo5.StreetNo5.domain.dto.UserPostsDto;
import com.StreetNo5.StreetNo5.service.GCSService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import com.StreetNo5.StreetNo5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserPostService userPostService;
    private final GCSService gcsService;
    private final UserService userService;


    //@PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "전체 게시물 조회 API")
    @GetMapping("/posts")
    public Slice<UserPostsDto> getBoardList(
            @PageableDefault (size = 6,sort = "createdDate",direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable) {
        List<UserPost> userPosts = userPostService.getUserPosts();
        return getUsersPostsDto(pageable,userPosts);
    }



    @Operation(summary = "인기 게시물(조회수 기준) 5개 조회 API")
    @GetMapping("/popular-post")
    public List<UserPostDto> getPopularPost()
    {
        List<UserPost> userPolarPost = userPostService.getUserPolarPost();
        return ConvertPopularDto(userPolarPost);
    }

    @Operation(summary = "게시물 번호를 이용한 게시물 조회 API")
    @GetMapping("/{id}")
    public UserPost getPost(@PathVariable Long id) {
        userPostService.updateView(id);
        return userPostService.getUserPost(id);
    }

    @Operation(summary = "게시글 작성 API")
    @PostMapping("/post/write")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String writePost(UserPost userPost, @RequestHeader(value = "Authorization") String token, MultipartFile imageFile) throws IOException {
        String nickname = getUserNicknameFromJwtToken(token);
        if (imageFile==null)
        {
            throw new IllegalArgumentException("이미지는 필수입니다.");
        }
        String imageUrl = gcsService.updateMemberInfo(imageFile);
        UserPost post = UserPost.builder()
                .content(userPost.getContent())
                .imageUrl(imageUrl)
                .lat(userPost.getLat())
                .lng(userPost.getLng())
                .tags(userPost.getTags())
                .tags_num(convertTags(userPost.getTags()))
                .music_id(userPost.getMusic_id())
                .build();
        Optional<User> user = userService.findUser(nickname);
        User user1 = user.get();
        user1.addPost(post);
        post.setUser(user.get());
        userPostService.writePost(post);
        return "OK";
    }



    private Slice<UserPostsDto> getUsersPostsDto(Pageable pageable, List<UserPost> userPosts) {
        List<UserPostsDto> userPostsLists = ConvertDto(userPosts);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), userPostsLists.size());
        boolean hasNext = false;
        if (userPostsLists.size()- pageable.getPageSize() > pageable.getOffset()) {
            hasNext = true;
        }
        Slice<UserPostsDto> slice = new SliceImpl<>(userPostsLists.subList(start, end), pageable, hasNext);
        return slice;
    }

    private List<UserPostsDto> ConvertDto(List<UserPost> userPosts) {
        List<UserPostsDto> userPostsLists = new ArrayList<>();
        for (UserPost userPost : userPosts){
            UserPostsDto userPostsDto=new UserPostsDto();
            userPostsDto.setId(userPost.getId());
            userPostsDto.setImageUrl(userPost.getImageUrl());
            userPostsLists.add(userPostsDto);
        }
        return userPostsLists;
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

    private List<UserPostDto> ConvertPopularDto(List<UserPost> userPosts) {
        List<UserPostDto> userPostsLists = new ArrayList<>();
        for (UserPost userPost : userPosts){
            UserPostDto userPostsDto=new UserPostDto();
            userPostsDto.setId(userPost.getId());
            userPostsDto.setNickname(userPost.getUser().getNickname());
            userPostsDto.setViewCount(userPost.getViewCount());
            userPostsDto.setImageUrl(userPost.getImageUrl());
            userPostsDto.setContent(userPost.getContent());
            userPostsDto.setAddress_name(userPost.getAddress_name());
            userPostsDto.setModifiedDate(userPost.getModifiedDate());
            userPostsDto.setTags(userPost.getTags());
            userPostsLists.add(userPostsDto);
        }
        return userPostsLists;
    }


}

