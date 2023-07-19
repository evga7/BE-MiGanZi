package com.StreetNo5.StreetNo5.service.redis;


import com.StreetNo5.StreetNo5.domain.dto.RoomMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, RoomMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);

    }

}