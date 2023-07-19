package com.StreetNo5.StreetNo5.config.redis;

import com.StreetNo5.StreetNo5.config.jwt.ExpireTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "userAlert", timeToLive = ExpireTime.REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS)
public class UserAlert {
    @Id
    private String id;
    private String title;
    private String message;
    private String createdDate;

    @Indexed
    private String nickname;

}
