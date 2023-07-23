package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.config.redis.UserAlert;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface UserAlertRedisRepository extends CrudRepository<UserAlert,String> {
    List<UserAlert> findByUserId(Long UserId);
}
