package com.StreetNo5.StreetNo5.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPositionDto {
    private Double lat;
    private Double lng;
    private String address_name;
    private String tags;

}
