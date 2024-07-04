package com.StreetNo5.StreetNo5.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
@Component
public class PreventDuplicateSubmitAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(preventDuplicateSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) throws Throwable {
        String key = generateKey(joinPoint, preventDuplicateSubmit);
        RLock lock = redissonClient.getLock(key);
        boolean isLocked = lock.tryLock(2000, 1000, TimeUnit.MILLISECONDS);
        if (!isLocked) {
            throw new RuntimeException("중복 요청이 감지되었습니다.");
        }
        try {
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    private String generateKey(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) {
        Object[] args = joinPoint.getArgs();
        String key = "userPost:" + args[0];
        return key;
    }
}