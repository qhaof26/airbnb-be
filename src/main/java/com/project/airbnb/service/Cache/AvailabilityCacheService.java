package com.project.airbnb.service.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String AVAILABILITY_KEY_PREFIX = "availability:";
    private static final long AVAILABILITY_CACHE_TTL = 24; // hours
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public void cacheAvailableDates(String listingId, List<LocalDate> availableDates) {
        try {
            String key = AVAILABILITY_KEY_PREFIX + listingId;

            // Convert dates to strings for storage
            List<String> dateStrings = availableDates.stream()
                    .map(date -> date.format(DATE_FORMATTER))
                    .toList();

            // Store in Redis
            redisTemplate.opsForSet().add(key, dateStrings.toArray());
            redisTemplate.expire(key, AVAILABILITY_CACHE_TTL, TimeUnit.HOURS);

            log.info("Cached {} available dates for listing ID: {}", availableDates.size(), listingId);
        } catch (Exception e) {
            log.error("Error caching available dates: {}", e.getMessage(), e);
        }
    }

    public void markDateBooked(String listingId, LocalDate date) {
        try {
            String key = AVAILABILITY_KEY_PREFIX + listingId;
            String dateString = date.format(DATE_FORMATTER);

            redisTemplate.opsForSet().remove(key, dateString);
            log.info("Marked date {} as booked for listing ID: {}", dateString, listingId);
        } catch (Exception e) {
            log.error("Error marking date as booked: {}", e.getMessage(), e);
        }
    }

    public void markDateAvailable(String listingId, LocalDate date) {
        try {
            String key = AVAILABILITY_KEY_PREFIX + listingId;
            String dateString = date.format(DATE_FORMATTER);

            redisTemplate.opsForSet().add(key, dateString);
            log.info("Marked date {} as available for listing ID: {}", dateString, listingId);
        } catch (Exception e) {
            log.error("Error marking date as available: {}", e.getMessage(), e);
        }
    }

    public List<LocalDate> getAvailableDates(String listingId) {
        try {
            String key = AVAILABILITY_KEY_PREFIX + listingId;
            Set<Object> dateStrings = redisTemplate.opsForSet().members(key);

            List<LocalDate> availableDates = new ArrayList<>();
            if (dateStrings != null) {
                for (Object obj : dateStrings) {
                    if (obj instanceof String) {
                        LocalDate date = LocalDate.parse((String) obj, DATE_FORMATTER);
                        availableDates.add(date);
                    }
                }
            }

            return availableDates;
        } catch (Exception e) {
            log.error("Error getting available dates: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void clearAvailabilityCache(String listingId) {
        try {
            String key = AVAILABILITY_KEY_PREFIX + listingId;
            redisTemplate.delete(key);
            log.info("Cleared availability cache for listing ID: {}", listingId);
        } catch (Exception e) {
            log.error("Error clearing availability cache: {}", e.getMessage(), e);
        }
    }
}
