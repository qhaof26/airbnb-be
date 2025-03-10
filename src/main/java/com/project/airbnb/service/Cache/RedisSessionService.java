package com.project.airbnb.service.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSessionService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SESSION_KEY_PREFIX = "session:";
    private static final long SESSION_TTL = 30; // minutes

    public void saveSession(String sessionId, Map<String, Object> sessionData) {
        String key = SESSION_KEY_PREFIX + sessionId;
        redisTemplate.opsForHash().putAll(key, sessionData);
        redisTemplate.expire(key, SESSION_TTL, TimeUnit.MINUTES);
    }

    public Map<Object, Object> getSession(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId;
        return redisTemplate.opsForHash().entries(key);
    }

    public void updateSession(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId;
        redisTemplate.expire(key, SESSION_TTL, TimeUnit.MINUTES);
    }

    public void invalidateSession(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId;
        redisTemplate.delete(key);
    }
}
