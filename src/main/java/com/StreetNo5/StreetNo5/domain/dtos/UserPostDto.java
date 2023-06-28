package com.StreetNo5.StreetNo5.domain.dtos;


import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserPostDto {
    private Long id;
    @Size(min = 2,max = 30,message = "제목을 2~30자 사이로 입력해주세요.")
    private String title;
    private String nickname;
    @Size(min = 2, max = 250,message = "내용은 2~500자 사이로 입력해주세요.")
    private String content;
    private String imageUrl;
    private String createdAt;
    private String modifiedAt;
    private List<UserPostCommentDto> comments;
    private Double lat;
    private Double lng;
    private String address_name;


}
