package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByNickname(String nickname);
    boolean existsUsersByNickname(String nickname);
}
