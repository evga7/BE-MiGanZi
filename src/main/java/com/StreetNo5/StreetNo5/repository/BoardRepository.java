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

    @Transactional
    @Modifying
    @Query(value = "update user_post p set p.profile_image =:newImageUrl and p.thumbnail_image=:newThumbnailUrl where p.post_id=:id",nativeQuery = true)
    void updatePostImageUrl(@Param("newImageUrl") String ImageUrl, @Param("newThumbnailUrl") String ThumbUrl ,@Param("id") Long id);

    @Query(value = "SELECT * FROM user_post u WHERE " +
            "ST_Distance_Sphere(POINT(u.lng, u.lat), POINT(:lng, :lat)) <= 5000 AND " +
            "(u.tags_num & :tagsNum) = :tagsNum",nativeQuery = true)
    List<UserPost> findNearPosts(@Param("lng") Double lng,
                                  @Param("lat") Double lat,
                                  @Param("tagsNum") Long tagsNum);

    List<UserPost> findByUser_Nickname(String nickname);





}
