package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    boolean existsUsersByNickname(String nickname);

/*    @Transactional
    @Modifying
    @Query(value = "update user p set p.password =:newPassword where p.nickname=:nickname",nativeQuery = true)
    void updatePassword(@Param("nickname") String nickname,@Param("password")String newPassword);*/
}
