package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.domain.UserPost;
import com.StreetNo5.StreetNo5.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPostService {
    private final BoardRepository boardRepository;
    public List<UserPost> getUserPosts(){
        return boardRepository.findAll();
    }
    public UserPost getUserPost(Long id){
        Optional<UserPost> byId = boardRepository.findById(id);
        return byId.get();
    }

    public void updateView(Long id)
    {
        boardRepository.updatePageView(id);
    }
    public void writePost(UserPost userPost){
        boardRepository.save(userPost);
    }
    public List<UserPost>getUserPostList(){
        return boardRepository.findAll();
    }
    public List<UserPost>getUserPolarPost(){
        return boardRepository.findPolarPost();
    }
}
