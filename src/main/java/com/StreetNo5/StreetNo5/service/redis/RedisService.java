package com.StreetNo5.StreetNo5.service.redis;

import com.StreetNo5.StreetNo5.config.redis.RefreshToken;
import com.StreetNo5.StreetNo5.config.redis.UserAlert;
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
    public RefreshToken findByRefresh(String refreshToken){
        return refreshTokenRedisRepository.findByRefreshToken(refreshToken);
    }
    public void saveRefreshToken(RefreshToken refreshToken){
        refreshTokenRedisRepository.save(refreshToken);
    }
    public void saveUserAlert(UserAlert userAlert){
        userAlertRedisRepository.save(userAlert);
    }
}
