package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.dto.RoomMessage;
import com.StreetNo5.StreetNo5.service.UserService;
import com.StreetNo5.StreetNo5.service.redis.RedisPublisher;
import com.StreetNo5.StreetNo5.service.redis.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RequestMapping("/pubsub")
@RestController
@RequiredArgsConstructor
public class PubSubController {

    // topic에 메시지 발행을 기다리는 Listner

    private final RedisMessageListenerContainer redisMessageListener;
    private final UserService userService;
    // 발행자
    private final RedisPublisher redisPublisher;
    // 구독자
    private final RedisSubscriber redisSubscriber;
    // topic 이름으로 topic정보를 가져와 메시지를 발송할 수 있도록 Map에 저장
    private Map<String, ChannelTopic> channels;

    @PostConstruct
    public void init() {
        // topic 정보를 담을 Map을 초기화
        channels = new HashMap<>();
        List<User> allUser = userService.findAlluser();
        allUser.stream().forEach(user-> channels.put(user.getNickname(),new ChannelTopic(user.getNickname())));

    }

    // 유효한 Topic 리스트 반환

    @GetMapping("/room")
    public Set<String> findAllRoom() {
        return channels.keySet();
    }

    // 신규 Topic을 생성하고 Listener등록 및 Topic Map에 저장

    @PutMapping("/room/{roomId}")
    public void createRoom(@PathVariable String roomId) {
        ChannelTopic channel = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        channels.put(roomId, channel);
    }


    // 특정 Topic에 메시지 발행

    @PostMapping("/room/{roomId}")
    public void pushMessage(@PathVariable String roomId, String title,String createdDate, String message) {
        ChannelTopic channel = channels.get(roomId);
        redisPublisher.publish(channel, RoomMessage.builder().title(title).createdDate(createdDate).roomId(roomId).message(message).build());
    }

    // Topic 삭제 후 Listener 해제, Topic Map에서 삭제
    @DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        ChannelTopic channel = channels.get(roomId);
        redisMessageListener.removeMessageListener(redisSubscriber, channel);
        channels.remove(roomId);
    }
}