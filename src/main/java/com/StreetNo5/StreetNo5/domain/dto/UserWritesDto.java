package com.StreetNo5.StreetNo5.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class UserWritesDto {
    Slice<PostsDto> postsDto;
    private int numberOfPosts;
}
