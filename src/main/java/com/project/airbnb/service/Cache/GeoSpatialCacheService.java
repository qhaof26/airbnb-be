package com.project.airbnb.service.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoSpatialCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String GEO_KEY = "geo:listings";
    private static final long GEO_CACHE_TTL = 24; // hours

    public void addListingLocation(String listingId, double longitude, double latitude) {
        try {
            redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), listingId);
            redisTemplate.expire(GEO_KEY, GEO_CACHE_TTL, TimeUnit.HOURS);
            log.info("Added listing location for ID: {}", listingId);
        } catch (Exception e) {
            log.error("Error adding listing location: {}", e.getMessage(), e);
        }
    }

    public List<String> findNearbyListings(double longitude, double latitude, double radiusInKm) {
        try {
            Circle circle = new Circle(new Point(longitude, latitude), new Distance(radiusInKm, Metrics.KILOMETERS));
            GeoResults<RedisGeoCommands.GeoLocation<Object>> results =
                    redisTemplate.opsForGeo().radius(GEO_KEY, circle);

            List<String> nearbyListingIds = new ArrayList<>();
            if (results != null) {
                for (GeoResult<RedisGeoCommands.GeoLocation<Object>> result : results) {
                    Object member = result.getContent().getName();
                    if (member instanceof String) {
                        nearbyListingIds.add((String) member);
                    }
                }
            }
            return nearbyListingIds;
        } catch (Exception e) {
            log.error("Error finding nearby listings: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void removeListingLocation(String listingId) {
        try {
            redisTemplate.opsForGeo().remove(GEO_KEY, listingId);
            log.info("Removed listing location for ID: {}", listingId);
        } catch (Exception e) {
            log.error("Error removing listing location: {}", e.getMessage(), e);
        }
    }
}
