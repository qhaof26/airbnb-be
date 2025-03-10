package com.project.airbnb.service.Cache;

import com.project.airbnb.dto.response.ListingResponseDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LISTING_CACHE_KEY = "listing:";
    private static final long LISTING_CACHE_TTL = 24; // hours

    public void cacheListingDetails(ListingResponseDetail listingResponse) {
        try {
            String key = LISTING_CACHE_KEY + listingResponse.getId();
            redisTemplate.opsForValue().set(key, listingResponse);
            redisTemplate.expire(key, LISTING_CACHE_TTL, TimeUnit.HOURS);
            log.info("Cached listing details for ID: {}", listingResponse.getId());
        } catch (Exception e) {
            log.error("Error caching listing details: {}", e.getMessage(), e);
        }
    }

    public ListingResponseDetail getListingFromCache(String listingId) {
        try {
            String key = LISTING_CACHE_KEY + listingId;
            Object cachedListing = redisTemplate.opsForValue().get(key);
            if (cachedListing instanceof ListingResponseDetail) {
                log.info("Cache hit for listing ID: {}", listingId);
                return (ListingResponseDetail) cachedListing;
            }
        } catch (Exception e) {
            log.error("Error retrieving listing from cache: {}", e.getMessage(), e);
        }
        log.info("Cache miss for listing ID: {}", listingId);
        return null;
    }

    public void evictListingCache(String listingId) {
        try {
            String key = LISTING_CACHE_KEY + listingId;
            redisTemplate.delete(key);
            log.info("Evicted listing cache for ID: {}", listingId);
        } catch (Exception e) {
            log.error("Error evicting listing cache: {}", e.getMessage(), e);
        }
    }
}
