package com.StreetNo5.StreetNo5.entity.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class UserSliceCommentsDto {
    Slice<MyCommentsDto> myCommentsDto;
    private int numberOfComments;
}
