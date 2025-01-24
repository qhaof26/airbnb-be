package com.project.airbnb.repositories;

import com.project.airbnb.models.Listing;
import com.project.airbnb.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByListingId(String listingId, Pageable pageable);
    @Query(value = "select count(r.id) from Review r where r.listing = :listing")
    Integer quantityReview(Listing listing);

    @Query(value = "select sum(r.star) from Review r where r.listing = :listing")
    Float sumStar(Listing listing);

}
