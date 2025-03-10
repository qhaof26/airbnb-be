package com.project.airbnb.service.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RATE_LIMIT_KEY = "rate:limit:";
    private static final int MAX_REQUESTS = 100; // Số lượng request tối đa
    private static final int WINDOW_SIZE_SECONDS = 60; // Khoảng thời gian (giây)

    public boolean allowRequest(String clientId) {
        String key = RATE_LIMIT_KEY + clientId;

        Long currentCount = redisTemplate.opsForValue().increment(key);
        if (currentCount == 1) {
            // Nếu là request đầu tiên, set thời gian hết hạn
            redisTemplate.expire(key, WINDOW_SIZE_SECONDS, TimeUnit.SECONDS);
        }

        return currentCount <= MAX_REQUESTS;
    }
}