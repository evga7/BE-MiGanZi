package com.StreetNo5.StreetNo5.repository;

import com.StreetNo5.StreetNo5.config.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
}