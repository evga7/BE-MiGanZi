package com.StreetNo5.StreetNo5.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCommentRequestDto {
    private Long postId;
    private String content;
}
