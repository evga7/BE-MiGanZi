package com.StreetNo5.StreetNo5.domain.dto;

import com.StreetNo5.StreetNo5.domain.UserComment;
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
    private Long viewCount=0L;
    private Long commentCount=0L;
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
