package com.StreetNo5.StreetNo5.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MyCommentsDto {

    private Long post_id;
    private String image_url;
    private String content;
    private String modifiedDate;
}
