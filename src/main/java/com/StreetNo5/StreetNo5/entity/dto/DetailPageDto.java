package com.StreetNo5.StreetNo5.entity.dto;

import com.StreetNo5.StreetNo5.entity.UserComment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class DetailPageDto {
    private Long id;
    private int viewCount;
    private int commentCount;
    private String content;
    private String imageUrl;
    private String address_name;
    private String tags;
    private String music_id;
    private String nickname;
    private String createdDate;
    private String modifiedDate;
    private String profileImage;
    private List<UserComment> userComments=new ArrayList<>();
}
