package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.domain.dtos.UserPositionDto;
import com.StreetNo5.StreetNo5.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/board")
@RequiredArgsConstructor
public class UserFindPostController {
    private final UserPostService userPostService;

    @PostMapping("/find-near-post")
    public List<UserPost> getBoardListFromUserSearch(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.ASC) Pageable pageable,
                                                     UserPositionDto userPositionDto) {
        List<UserPost> userPostData= userPostService.getUserPostList();
        List<UserPost> userPostList = new ArrayList<>();
        for (int i=0;i<userPostData.size();i++){
            if (getDistance(userPositionDto.getLat(),userPositionDto.getLng(),userPostData.get(i).getUserPositionInfo().getLat(),userPostData.get(i).getUserPositionInfo().getLng())<=2){
                userPostList.add(userPostData.get(i));
            }
        }
        return userPostList;
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



}
