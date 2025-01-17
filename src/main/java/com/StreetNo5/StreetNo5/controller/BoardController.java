package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.entity.User;
import com.StreetNo5.StreetNo5.entity.UserPost;
import com.StreetNo5.StreetNo5.entity.dto.ApiResponse;
import com.StreetNo5.StreetNo5.entity.dto.DetailPageDto;
import com.StreetNo5.StreetNo5.entity.dto.PostDto;
import com.StreetNo5.StreetNo5.entity.dto.PostsDto;
import com.StreetNo5.StreetNo5.entity.dto.request.UserPostRequestDto;
import com.StreetNo5.StreetNo5.jwt.JwtTokenProvider;
import com.StreetNo5.StreetNo5.service.ImageService;
import com.StreetNo5.StreetNo5.service.UserPostService;
import com.StreetNo5.StreetNo5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class BoardController {

    private final ApiResponse apiResponse;
    private final UserPostService userPostService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageService imageService;
    @Value("${gcp.bucket.url}")
    private String bucketUrl;
    @Value("${profile.image.url}")
    private String profileImage;


    //@PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "전체 게시물 조회 API")
    @GetMapping("/posts")
    public Slice<PostsDto> getBoardList(
            @PageableDefault (size = 6,sort = "createdDate",direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable) {
        List<UserPost> userPosts = userPostService.getUserPosts();
        return getUsersPostsDto(pageable,userPosts);
    }



    @Operation(summary = "인기 게시물(조회수 기준) 5개 조회 API")
    @GetMapping("/popular-post")
    public List<PostDto> getPopularPost()
    {
        List<UserPost> userPolarPost = userPostService.getUserPolarPost();
        return ConvertPopularDto(userPolarPost);
    }

    @Operation(summary = "게시물 번호를 이용한 게시물 조회 API")
    @GetMapping("/{postId}")
    public DetailPageDto getPost(@PathVariable Long postId) {
        UserPost userPost = userPostService.getUserPost(postId);
        userPostService.increasePostViewCount(postId);
        return DetailPageDto.builder()
                .id(userPost.getId())
                .viewCount(userPost.getViewCount())
                .content(userPost.getContent())
                .commentCount(userPost.getUserComments().size())
                .imageUrl(userPost.getDetailImageUrl())
                .profileImage(userPost.getProfileImage())
                .address_name(userPost.getAddress_name())
                .tags(userPost.getTags())
                .music_id(userPost.getMusic_id())
                .nickname(userPost.getUser().getNickname())
                .userComments(userPost.getUserComments())
                .createdDate(userPost.getCreatedDate())
                .modifiedDate(userPost.getModifiedDate())
                .build();
    }


    @Operation(summary = "게시글 작성 API")
    @PostMapping("/write")
    public ResponseEntity<?> writePost(UserPostRequestDto userPost, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        String nickname = getUserNicknameFromJwtToken(token);
        if (userPost.getContent().length() < 2 || userPost.getContent().length() > 500) {
            return apiResponse.fail("내용은 2~500자 사이로 입력해주세요.");
        }

        // 기본 이미지 URL 설정
        String uploadingImageUrl = bucketUrl+"uploading.png";

        UserPost post = UserPost.builder()
                .content(userPost.getContent())
                .detailImageUrl(uploadingImageUrl)
                .thumbnailImageUrl(uploadingImageUrl)
                .lat(userPost.getLat())
                .lng(userPost.getLng())
                .tags(userPost.getTags())
                .profileImage(profileImage)
                .tagsNum(convertTags(userPost.getTags()))
                .address_name(userPost.getAddress_name())
                .music_id(userPost.getMusic_id())
                .build();
        Optional<User> user = userService.findUser(nickname);
        if (!user.isPresent()) {
            return apiResponse.fail("유저 에러", HttpStatus.BAD_REQUEST);
        }
        User user1 = user.get();
        user1.addPost(post);
        post.setUser(user.get());
        userPostService.writePost(post);

        // 비동기로 이미지 리사이즈 작업 수행
        Future<String> detailImageFuture = imageService.resizeAndUploadDetailImage(userPost.getImageFile());
        Future<String> thumbnailImageFuture = imageService.resizeAndUploadThumbnailImage(userPost.getImageFile());

        // 리사이즈된 이미지 URL로 업데이트
        CompletableFuture.runAsync(() -> {
            try {
                String detailImageUrl = detailImageFuture.get();
                String thumbnailImageUrl = thumbnailImageFuture.get();

                post.setDetailImageUrl(detailImageUrl);
                post.setThumbnailImageUrl(thumbnailImageUrl);
                userPostService.writePost(post); // DB 업데이트

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        return apiResponse.success("성공", HttpStatus.ACCEPTED);
    }




    private Slice<PostsDto> getUsersPostsDto(Pageable pageable, List<UserPost> userPosts) {
        List<PostsDto> userPostsLists = ConvertDto(userPosts);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), userPostsLists.size());
        boolean hasNext = false;
        if (userPostsLists.size()- pageable.getPageSize() > pageable.getOffset()) {
            hasNext = true;
        }
        Slice<PostsDto> slice = new SliceImpl<>(userPostsLists.subList(start, end), pageable, hasNext);
        return slice;
    }

    private List<PostsDto> ConvertDto(List<UserPost> userPosts) {
        List<PostsDto> userPostsLists = new ArrayList<>();
        for (UserPost userPost : userPosts){
            PostsDto userPostsDto=new PostsDto();
            userPostsDto.setId(userPost.getId());
            userPostsDto.setImageUrl(userPost.getThumbnailImageUrl());
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

    private List<PostDto> ConvertPopularDto(List<UserPost> userPosts) {
        List<PostDto> userPostsLists = new ArrayList<>();
        for (UserPost userPost : userPosts){
            userPostsLists.add(PostDto.builder()
                    .id(userPost.getId())
                    .nickname(userPost.getUser().getNickname())
                    .viewCount(userPost.getViewCount())
                    .imageUrl(userPost.getDetailImageUrl())
                    .content(userPost.getContent())
                    .profileImage(userPost.getProfileImage())
                    .address_name(userPost.getAddress_name())
                    .modifiedDate(userPost.getModifiedDate())
                    .tags(userPost.getTags())
                    .createdDate(userPost.getCreatedDate()).build());
        }
        return userPostsLists;
    }


}

