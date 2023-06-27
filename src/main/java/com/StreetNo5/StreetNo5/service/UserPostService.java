package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPostService {
    private final BoardRepository boardRepository;
    public Page<UserPost> getUserPosts(Pageable pageable){
        return boardRepository.findSliceBy(pageable);
    }
    public UserPost getUserPost(Long id){
        Optional<UserPost> byId = boardRepository.findById(id);
        return byId.get();
    }

    public void updateView(Long id)
    {
        boardRepository.updatePageView(id);
    }
    public void updateCommentCount(Long id)
    {
        boardRepository.upCommentCount(id);
    }
    public void writePost(UserPost userPost){
        boardRepository.save(userPost);
    }
}
