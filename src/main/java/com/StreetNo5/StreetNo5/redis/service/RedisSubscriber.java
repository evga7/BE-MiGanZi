package com.StreetNo5.StreetNo5.redis.service;

import com.StreetNo5.StreetNo5.entity.dto.RoomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            RoomMessage roomMessage = objectMapper.readValue(body, RoomMessage.class);

        } catch (Exception e) {
        }
    }
}