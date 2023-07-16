package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    boolean existsUsersByNickname(String nickname);
}
