package com.StreetNo5.StreetNo5.domain.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomMessage implements Serializable {
    private static final long serialVersionUID = 2082503192322391880L;
    private String roomId;
    private String title;
    private String message;
    private String createdDate;

}