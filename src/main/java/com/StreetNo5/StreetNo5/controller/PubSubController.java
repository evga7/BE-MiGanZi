package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.config.redis.UserAlert;
import com.StreetNo5.StreetNo5.domain.User;
import com.StreetNo5.StreetNo5.domain.dto.RoomMessage;
import com.StreetNo5.StreetNo5.repository.UserAlertRedisRepository;
import com.StreetNo5.StreetNo5.service.UserService;
import com.StreetNo5.StreetNo5.service.redis.RedisPublisher;
import com.StreetNo5.StreetNo5.service.redis.RedisSubscriber;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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
    private final UserAlertRedisRepository userAlertRedisRepository;

    @PostConstruct
    public void init() {
        // topic 정보를 담을 Map을 초기화
        channels = new HashMap<>();
        List<User> allUser = userService.findAlluser();
        allUser.stream().forEach(user-> channels.put(user.getNickname(),new ChannelTopic(user.getNickname())));

    }

    // 유효한 Topic 리스트 반환

    public Set<String> findAllRoom() {
        return channels.keySet();
    }

    @Operation(summary = "유저 알림 정보 확인 API")
    @GetMapping("/user-alert")
    public List<UserAlert> getUserAlert(@RequestHeader(value = "Authorization") String token){
        List<UserAlert> byNickname = userAlertRedisRepository.findByNickname(getUserNicknameFromJwtToken(token));
        return byNickname;
    }

    // 신규 Topic을 생성하고 Listener등록 및 Topic Map에 저장

    //@PutMapping("/room/{roomId}")
    public void createRoom(@PathVariable String roomId) {
        ChannelTopic channel = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        channels.put(roomId, channel);
    }


    // 특정 Topic에 메시지 발행

    //@PostMapping("/room/{roomId}")
    public void pushMessage(@PathVariable String roomId, String title,String createdDate, String message) {
        ChannelTopic channel = channels.get(roomId);
        redisPublisher.publish(channel, RoomMessage.builder().title(title).createdDate(createdDate).roomId(roomId).message(message).build());
    }

    // Topic 삭제 후 Listener 해제, Topic Map에서 삭제
    //@DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        ChannelTopic channel = channels.get(roomId);
        redisMessageListener.removeMessageListener(redisSubscriber, channel);
        channels.remove(roomId);
    }

    private String getUserNicknameFromJwtToken(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitJwt = token.split("\\.");
        final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
        String nickname = payloadStr.split(":")[1].replace("\"", "").split(",")[0];
        return nickname;
    }
}