package com.project.airbnb.repositories;

import com.project.airbnb.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, String> {
    int countByObjectId(String objectId);

    @Query("select i.url from Image i where i.objectId = :listingId order by i.createdAt asc limit 1")
    Optional<String> findAvatarListing(@Param("listingId") String listingId);

    @Query("select i.url from Image i where i.objectId = :listingId ")
    Optional<Set<String>> findImagesListing(@Param("listingId") String listingId);
}
