package com.project.airbnb.service.Redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BOOKING_NOTIFICATION_CHANNEL = "booking:notifications";
    private static final String REVIEW_NOTIFICATION_CHANNEL = "review:notifications";

    public void publishBookingNotification(Object message) {
        try {
            redisTemplate.convertAndSend(BOOKING_NOTIFICATION_CHANNEL, message);
            log.info("Published booking notification");
        } catch (Exception e) {
            log.error("Error publishing booking notification: {}", e.getMessage(), e);
        }
    }

    public void publishReviewNotification(Object message) {
        try {
            redisTemplate.convertAndSend(REVIEW_NOTIFICATION_CHANNEL, message);
            log.info("Published review notification");
        } catch (Exception e) {
            log.error("Error publishing review notification: {}", e.getMessage(), e);
        }
    }
}
