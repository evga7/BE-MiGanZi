package com.StreetNo5.StreetNo5.controller;


import com.StreetNo5.StreetNo5.entity.User;
import com.StreetNo5.StreetNo5.entity.UserComment;
import com.StreetNo5.StreetNo5.entity.UserPost;
import com.StreetNo5.StreetNo5.entity.dto.ApiResponse;
import com.StreetNo5.StreetNo5.entity.dto.PostsDto;
import com.StreetNo5.StreetNo5.entity.dto.SignupForm;
import com.StreetNo5.StreetNo5.entity.dto.UserWritesDto;
import com.StreetNo5.StreetNo5.entity.dto.request.LoginForm;
import com.StreetNo5.StreetNo5.entity.dto.request.UserUpdateNickname;
import com.StreetNo5.StreetNo5.entity.dto.request.UserUpdatePassword;
import com.StreetNo5.StreetNo5.entity.dto.request.UserWithdrawal;
import com.StreetNo5.StreetNo5.entity.dto.response.MyCommentsDto;
import com.StreetNo5.StreetNo5.entity.dto.response.UserSliceCommentsDto;
import com.StreetNo5.StreetNo5.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PubSubController pubSubController;
    private final ApiResponse apiResponse;
    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, LoginForm loginForm) {

        return userService.login(request, loginForm.getNickname(), loginForm.getPassword());
    }

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid SignupForm signupForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
            }

            return apiResponse.fail(errorMap);
        }
        Long signup = userService.signup(signupForm);
        if (signup==-1L){
            return apiResponse.fail("중복된 유저입니다.");
        }
        if (signupForm.getNickname().equals(signupForm.getNickname())){
            pubSubController.createRoom(signupForm.getNickname());
        }
        return apiResponse.success("회원가입에 성공했습니다.");
    }

    @Operation(summary = "닉네임 변경 API",description = "요청값 accessToken 이며 만료된 액세스 토큰으로 요청시 거부합니다.")
    @PostMapping("/update-nickname")
    public ResponseEntity<?> changeUserNickName(@RequestHeader(value = "Authorization") @Parameter(hidden = true) String token, UserUpdateNickname userUpdateNickname)
    {
        return userService.changeNickName(token.substring(7),getUserNicknameFromJwtToken(token),userUpdateNickname.getNewNickname());
    }

    @Operation(summary = "로그아웃 API",description = "요청값 accessToken 이며 만료된 액세스 토큰으로 요청시 거부합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(@RequestHeader(value = "Authorization") @Parameter(hidden = true) String token)
    {
        return userService.logout(token.substring(7));
    }

    @Operation(summary = "회원탈퇴 API",description = "요청값 accessToken 이며 만료된 액세스 토큰으로 요청시 거부합니다.")
    @PostMapping("/withdrawal")
    public ResponseEntity<?> userWithdrawal(@RequestHeader(value = "Authorization") @Parameter(hidden = true) String token , UserWithdrawal userWithdrawal)
    {
        String nickname = getUserNicknameFromJwtToken(token);
        ResponseEntity<?> withdrawal = userService.withdrawal(token.substring(7), nickname,userWithdrawal.getCheckNickname());
        //TODO deleteRomm 검증후 delete
        pubSubController.deleteRoom(nickname);
        return withdrawal;
    }

    @Operation(summary = "비밀번호 변경 API",description = "요청값 accessToken 이며 만료된 액세스 토큰으로 요청시 거부합니다.")
    @PostMapping("/update-password")
    public ResponseEntity<?> changePassword(@RequestHeader(value = "Authorization") @Parameter(hidden = true) String token, @Valid UserUpdatePassword userUpdatePassword
    ,BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
            }

            return apiResponse.fail(errorMap);
        }
        return userService.changePassword(token.substring(7),userUpdatePassword.getNewPassword());
    }


    @Operation(summary = "닉네임 조건 확인 API",description = "반환값 String, 닉네임 존재시 extis," +
            "빈값 empty," +
            "8글자 이상 length," +
            "특수문자나 닉네임 원칙 안맞으면 notMatch , " +
            "정상적일시 OK 반환 입니다 그냥 OK 이외에는 다 안되는 값이라고 판한해서 값을 return 합니다.")
    @GetMapping("/check/{nickname}")
    public String checkNickNameDuplicate(@PathVariable String nickname) {
        boolean matches = nickname.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
        if (userService.checkNickNameExists(nickname))
            return "exits";
        else if (nickname.contains("운영자")||nickname.contains("admin"))
            return "noNickName";
        else if (nickname.isEmpty())
            return "empty";
        else if (nickname.length()>8)
            return "length";
        else if (!matches)
            return "notMatch";
        else
            return "OK";
    }


    @Operation(summary = "유저 게시글 받아오는 API",description = "게시물 + numberOfPosts")
    @GetMapping("/my-page/posts")
    public UserWritesDto getUserPosts(@PageableDefault(size = 6,sort = "modifiedDate",direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable,
                                      @RequestHeader(value = "Authorization") @Parameter(hidden = true) String token){
        String userNicknameFromJwtToken = getUserNicknameFromJwtToken(token);
        // jwt 검증 return 필요없음 따로 하는듯
        List<UserPost> userPosts = userService.getUserPosts(userNicknameFromJwtToken);
        userPosts.sort(Comparator.comparing(UserPost::getModifiedDate).reversed());
        Slice<PostsDto> usersPostsDto = getUsersPostsDto(pageable, userPosts);
        UserWritesDto userWritesDto = new UserWritesDto();
        userWritesDto.setPostsDto(usersPostsDto);
        userWritesDto.setNumberOfPosts(userPosts.size());
        return userWritesDto;
    }


    //내용 그림 시간
    @Operation(summary = "유저 댓글 받아오는 API",description = "게시물 + numberOfComments")
    @GetMapping("/my-page/comments")
    public UserSliceCommentsDto getUserComments(@PageableDefault(size = 6,sort = "modifiedDate",direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable,
                                                @RequestHeader(value = "Authorization") @Parameter(hidden = true) String token){
        String userNicknameFromJwtToken = getUserNicknameFromJwtToken(token);
        // todo jwt 검증후 return
        List<UserComment> userCommentsInfo = userService.getUserComments(userNicknameFromJwtToken);
        userCommentsInfo.sort(Comparator.comparing(UserComment::getModifiedDate).reversed());
        Slice<MyCommentsDto> MyCommentsDto = getUserCommentsDto(pageable, userCommentsInfo);
        UserSliceCommentsDto userCommentsDto = new UserSliceCommentsDto();
        userCommentsDto.setMyCommentsDto(MyCommentsDto);
        userCommentsDto.setNumberOfComments(userCommentsInfo.size());
        return userCommentsDto;
    }

    @PostMapping(value = "/reissue")
    @Operation(summary = "액세스 만료후 토큰 다시 받는 API",description = "요청값 refreshToken (액세스 토큰 제외) 반환값 accessToken" +
            " 이전 작동방식 -> (액세스 토큰 만료,리프래시 토큰 정상) 일때만 작동합니다.)" +
            " 현재 작동방식 -> (리프래시 토큰이 정상이면 액세스 토큰 관계 없이 작동합니다!)")
    public ResponseEntity<?> reissue(HttpServletRequest httpServletRequest) {
        return userService.reissue(httpServletRequest);
    }

    private String getUserNicknameFromJwtToken(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitJwt = token.split("\\.");
        final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
        String nickname = payloadStr.split(":")[1].replace("\"", "").split(",")[0];
        return nickname;
    }



    public List<User> getAllUser(){
        return userService.findAlluser();
    }
    // 댓글 변환
    private Slice<MyCommentsDto> getUserCommentsDto(Pageable pageable, List<UserComment> userComments) {
        List<MyCommentsDto> myCommentsDtos = ConvertCommentsDto(userComments);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), myCommentsDtos.size());
        boolean hasNext = false;
        if (myCommentsDtos.size()- pageable.getPageSize() > pageable.getOffset()) {
            hasNext = true;
        }
        Slice<MyCommentsDto> slice = new SliceImpl<>(myCommentsDtos.subList(start, end), pageable, hasNext);
        return slice;
    }

    private List<MyCommentsDto> ConvertCommentsDto(List<UserComment> userComments) {
        List<MyCommentsDto> myCommentsDtos = new ArrayList<>();
        for (UserComment userComment : userComments){
            MyCommentsDto myCommentsDto = MyCommentsDto.builder()
                    .content(userComment.getContent())
                    .post_id(userComment.getUserPost().getId())
                    .image_url(userComment.getUserPost().getThumbnailImageUrl())
                    .modifiedDate(userComment.getModifiedDate())
                    .build();
            myCommentsDtos.add(myCommentsDto);

        }
        return myCommentsDtos;
    }
    //댓글  끝

    //게시글 변환
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
    //게시글 끝




}
