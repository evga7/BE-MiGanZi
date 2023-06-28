package com.StreetNo5.StreetNo5.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserPositionInfo {

    @Id
    private Long id;
    private Double lat;
    private Double lng;
    private String address_name;

}
