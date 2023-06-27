package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.domain.UserComment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<UserComment,Long> {

}
