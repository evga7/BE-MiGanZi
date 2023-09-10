package com.StreetNo5.StreetNo5.redis.service;

import com.StreetNo5.StreetNo5.redis.RefreshToken;
import com.StreetNo5.StreetNo5.redis.UserAlert;
import com.StreetNo5.StreetNo5.repository.RefreshTokenRedisRepository;
import com.StreetNo5.StreetNo5.repository.UserAlertRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final UserAlertRedisRepository userAlertRedisRepository;

    public void removeRefreshToken(RefreshToken refreshToken){
        refreshTokenRedisRepository.delete(refreshToken);
    }

    public Optional<RefreshToken> findRefreshTokenById(String id){
        return refreshTokenRedisRepository.findById(id);

    }
    public RefreshToken findByRefreshString(String refreshToken){
        return refreshTokenRedisRepository.findByRefreshToken(refreshToken);
    }
    public void saveRefreshToken(RefreshToken refreshToken){
        refreshTokenRedisRepository.save(refreshToken);
    }
    public void saveUserAlert(UserAlert userAlert){
        userAlertRedisRepository.save(userAlert);
    }
}
