package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.config.redis.UserAlert;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface UserAlertRedisRepository extends CrudRepository<UserAlert,String> {
}
