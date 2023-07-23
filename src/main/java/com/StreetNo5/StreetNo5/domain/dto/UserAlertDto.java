package com.StreetNo5.StreetNo5.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAlertDto {
    private String title;
    private String message;
    private String createdDate;
}
