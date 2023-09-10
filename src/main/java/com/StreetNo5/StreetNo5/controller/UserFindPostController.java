package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.entity.UserPost;
import com.StreetNo5.StreetNo5.entity.dto.PostsDto;
import com.StreetNo5.StreetNo5.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class UserFindPostController {
    private final UserPostService userPostService;

    @Operation(summary = "주변 게시물 찾기 API")
    @GetMapping("/find-near-post/{lat}/{lng}/{tags}")
    public Slice<PostsDto> getBoardListFromUserSearch(@PageableDefault(size = 6,sort = "createdDate",direction = Sort.Direction.DESC) @Parameter(hidden = true) Pageable pageable
    , @PathVariable Double lat, @PathVariable Double lng, @PathVariable String tags) {
        List<UserPost> userPostData= userPostService.getUserPostList();
        List<UserPost> userPostList = new ArrayList<>();
        Long tags_num=convertTags(tags);
        for (int i=0;i<userPostData.size();i++){
            if (getDistance(lat,lng,userPostData.get(i).getLat(),userPostData.get(i).getLng())<=5
                    && ((tags_num&userPostData.get(i).getTagsNum()))==tags_num){
                userPostList.add(userPostData.get(i));
            }
        }
        return getUserPostsDto(pageable,userPostList);
    }

    // km 기준
    private Double getDistance(Double lat, Double lnt, Double lat2, Double lnt2) {
        double theta = lnt - lnt2;
        double dist = Math.sin(deg2rad(lat))* Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist / 1000;
    }

    //10진수를 radian(라디안)으로 변환
    private static double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private static double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }

    private Long convertTags(String tags){
        return Long.parseLong(tags,2);
    }

    private Slice<PostsDto> getUserPostsDto(Pageable pageable, List<UserPost> userPosts) {
        List<PostsDto> userPostsLists = new ArrayList<>();
        for (UserPost userPost : userPosts){
            PostsDto userPostsDto=new PostsDto();
            userPostsDto.setId(userPost.getId());
            userPostsDto.setImageUrl(userPost.getThumbnailImageUrl());
            userPostsLists.add(userPostsDto);
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), userPostsLists.size());
        boolean hasNext = false;
        if (userPostsLists.size()- pageable.getPageSize() > pageable.getOffset()) {
            hasNext = true;
        }
        Slice<PostsDto> slice = new SliceImpl<>(userPostsLists.subList(start, end), pageable, hasNext);
        return slice;
    }


}
