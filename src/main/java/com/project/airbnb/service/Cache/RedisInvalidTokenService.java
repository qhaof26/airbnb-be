package com.project.airbnb.service.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisInvalidTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    public void addToBlacklist(String jit, long ttl){
        String key = TOKEN_BLACKLIST_PREFIX + jit;
        redisTemplate.opsForValue().set(key, "1", ttl, TimeUnit.SECONDS);
        log.info("Added token to blacklist with TTL: {} seconds", ttl);
    }

    public boolean isBlacklisted(String jwtId) {
        String key = TOKEN_BLACKLIST_PREFIX + jwtId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public long calculateTtl(Date expiryTime) {
        long expiryTimeMs = expiryTime.getTime();
        long currentTimeMs = System.currentTimeMillis();
        long ttlMs = expiryTimeMs - currentTimeMs;

        return Math.max(0, TimeUnit.MILLISECONDS.toSeconds(ttlMs));
    }
}
