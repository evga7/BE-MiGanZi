package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.entity.UserPost;
import com.StreetNo5.StreetNo5.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<UserPost> getUserPosts(String nickname){
        return boardRepository.findByUser_Nickname(nickname);
    }
}
