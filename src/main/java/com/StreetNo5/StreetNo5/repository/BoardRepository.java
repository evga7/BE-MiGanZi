package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.domain.UserPost;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<UserPost,Long> {
    @Transactional
    @Modifying
    @Query(value = "update user_post p set p.view_count = p.view_count + 1 where p.post_id=:id",nativeQuery = true)
    void updatePageView(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query(value = "update user_post p set p.comment_count = p.comment_count + 1 where p.post_id=:id",nativeQuery = true)
    void upCommentCount(@Param("id") Long id);

    Page<UserPost> findSliceBy(Pageable pageable);



}
