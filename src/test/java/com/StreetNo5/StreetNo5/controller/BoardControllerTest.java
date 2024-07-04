package com.StreetNo5.StreetNo5.controller;

import com.StreetNo5.StreetNo5.entity.UserPost;
import com.StreetNo5.StreetNo5.repository.BoardRepository;
import com.StreetNo5.StreetNo5.service.UserPostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BoardControllerTest {

    @Autowired
    UserPostService userPostService;

    @Autowired
    BoardRepository boardRepository;

    private final Integer Count = 100;

    @Test
    @DisplayName("동시에 100명의 게시물 조회 : 동시성 이슈")
    public void badViewTest() throws Exception {
        request100Posts((_no) -> userPostService.increasePostViewCount(1L));
    }
    private void request100Posts(Consumer<Void> action) throws InterruptedException {

        Optional<UserPost> byId = boardRepository.findById(1L);
        int beforeViewCount = byId.get().getViewCount();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(Count);

        for (int i = 0; i < Count; i++) {
            executorService.submit(() -> {
                try {
                    action.accept(null);
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Optional<UserPost> byId2 = boardRepository.findById(1L);

        assertThat(byId2.get().getViewCount()).isEqualTo(beforeViewCount+ Count);
    }

    @Test
    @DisplayName("동시 100명 게시물 조회 : 분산락")
    public void redissonViewingTest() throws Exception {
        request100Posts((_no) -> userPostService.redissonIncreasePostViewCount(1L));
    }
}