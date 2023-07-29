package com.StreetNo5.StreetNo5.domain.dto.response;

import com.StreetNo5.StreetNo5.domain.UserComment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserListCommentsDto {
    List<UserComment> CommentsDto;
    private int numberOfComments;
}
