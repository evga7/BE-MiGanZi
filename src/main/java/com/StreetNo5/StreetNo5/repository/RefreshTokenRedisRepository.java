package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.redis.RefreshToken;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String refreshToken);
}