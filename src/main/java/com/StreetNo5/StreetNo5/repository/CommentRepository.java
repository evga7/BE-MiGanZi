package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.entity.UserComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<UserComment,Long> {
    List<UserComment> findByNickname(String nickname);

}
