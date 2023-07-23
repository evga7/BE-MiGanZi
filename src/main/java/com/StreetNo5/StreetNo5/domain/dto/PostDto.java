package com.StreetNo5.StreetNo5.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String nickname;
    private Long viewCount=0L;
    private String content;
    private String imageUrl;
    private String address_name;
    private String tags;
    private String modifiedDate;


}
