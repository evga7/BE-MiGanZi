package com.StreetNo5.StreetNo5.entity.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCommentRequestDto {
    private Long postId;
    private String content;
}
