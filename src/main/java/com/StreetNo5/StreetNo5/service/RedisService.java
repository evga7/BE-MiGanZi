package com.StreetNo5.StreetNo5.service;

import com.StreetNo5.StreetNo5.config.redis.RefreshToken;
import com.StreetNo5.StreetNo5.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public void removeRefreshToken(RefreshToken refreshToken){
        refreshTokenRedisRepository.delete(refreshToken);
    }
    public RefreshToken findByRefresh(String refreshToken){
        return refreshTokenRedisRepository.findByRefreshToken(refreshToken);
    }
}
