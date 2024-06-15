package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.entity.UserComment;
import com.StreetNo5.StreetNo5.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public String write_comment(UserComment comment){
        commentRepository.save(comment);
        return "OK";
    }
    public List<UserComment> getUserCommentsInfo(String nickname){
        return commentRepository.findByNickname(nickname);
    }

}
