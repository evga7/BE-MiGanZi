package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.entity.UserPost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<UserPost,Long> {
    @Transactional
    @Modifying
    @Query(value = "update user_post p set p.view_count = p.view_count + 1 where p.post_id=:id",nativeQuery = true)
    void updatePageView(@Param("id") Long id);

    @Query(value = "select u from UserPost u left join fetch u.user order by u.viewCount desc limit 5")
    List<UserPost> findPolarPost();

    @Query(value = "select u from UserPost u left join fetch u.user" )
    List<UserPost> findAllPostFetchJoin();

    List<UserPost> findByUser_Nickname(String nickname);





}
