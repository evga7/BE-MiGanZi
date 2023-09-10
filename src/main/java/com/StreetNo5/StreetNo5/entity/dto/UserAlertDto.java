package com.StreetNo5.StreetNo5.entity.dto;


import lombok.*;

@Getter
@Setter
@Builder
public class UserAlertDto {
    private String title;
    private String message;
    private String createdDate;
}
