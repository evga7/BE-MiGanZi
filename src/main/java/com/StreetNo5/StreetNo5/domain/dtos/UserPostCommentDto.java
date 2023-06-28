package com.StreetNo5.StreetNo5.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPostCommentDto {
    private Long id;
    private String nickname;
    @NotEmpty(message = "댓글을 입력해주세요")
    @Size(min = 1,max = 45,message = "댓글은 1~45자 사이로 입력해주세요.")
    private String content;
    private String createdDate;
    private String modifiedDate;
}
