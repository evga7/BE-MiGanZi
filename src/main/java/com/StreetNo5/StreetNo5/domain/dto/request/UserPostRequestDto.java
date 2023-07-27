package com.StreetNo5.StreetNo5.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserPostRequestDto {
    private String content;
    private Double lat;
    private Double lng;
    private String address_name;
    private String tags;
    private String music_id;
    private MultipartFile imageFile;
}
