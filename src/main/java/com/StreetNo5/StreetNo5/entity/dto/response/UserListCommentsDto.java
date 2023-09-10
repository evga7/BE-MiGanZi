package com.StreetNo5.StreetNo5.entity.dto.response;

import com.StreetNo5.StreetNo5.entity.UserComment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserListCommentsDto {
    List<UserComment> CommentsDto;
    private int numberOfComments;
}
