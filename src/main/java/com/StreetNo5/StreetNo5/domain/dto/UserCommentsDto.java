package com.StreetNo5.StreetNo5.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class UserCommentsDto {
    Slice<MyCommentsDto> myCommentsDto;
    private int numberOfComments;
}
