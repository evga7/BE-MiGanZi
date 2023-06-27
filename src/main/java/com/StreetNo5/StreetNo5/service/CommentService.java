package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.domain.UserComment;
import com.StreetNo5.StreetNo5.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public String write_comment(UserComment comment){
        commentRepository.save(comment);
        return "OK";
    }

}
