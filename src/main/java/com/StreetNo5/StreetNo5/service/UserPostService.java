package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.aspect.PreventDuplicateSubmit;
import com.StreetNo5.StreetNo5.entity.UserPost;
import com.StreetNo5.StreetNo5.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPostService {
    private final BoardRepository boardRepository;
    @Transactional(readOnly = true)
    public List<UserPost> getUserPosts(){
        return boardRepository.findAllPostFetchJoin();
    }

    @Transactional(readOnly = true)
    public UserPost getUserPost(Long id){
        Optional<UserPost> byId = boardRepository.findById(id);
        return byId.get();
    }

    public void updateView(Long id)
    {
        boardRepository.updatePageView(id);
    }
    public void increasePostViewCount(Long id)
    {
        UserPost userPost = boardRepository.findById(id).orElseThrow();
        userPost.increase();
        boardRepository.save(userPost);
    }
    @PreventDuplicateSubmit(key = "#postId")
    public void redissonIncreasePostViewCount(Long postId)
    {
        UserPost userPost = boardRepository.findById(postId).orElseThrow();
        userPost.increase();
        boardRepository.save(userPost);
    }
    @Transactional
    public void writePost(UserPost userPost){
        boardRepository.save(userPost);
    }
    @Transactional
    public void updatePost(UserPost userPost){
        boardRepository.updatePostImageUrl(userPost.getDetailImageUrl(), userPost.getThumbnailImageUrl(), userPost.getId());
    }
    @Transactional(readOnly = true)
    public List<UserPost>getUserPostList(){
        return boardRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<UserPost>getUserPolarPost(){
        return boardRepository.findPolarPost();
    }

}
