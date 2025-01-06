package com.project.airbnb.repositories;

import com.project.airbnb.models.Listing;
import com.project.airbnb.models.ListingAvailability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ListingAvailabilityRepository extends JpaRepository<ListingAvailability, String>, JpaSpecificationExecutor<ListingAvailability> {
    Page<ListingAvailability> findByListing(Listing listing, Pageable pageable);

    @Query("select la from ListingAvailability la where la.listing.id = :listingId and la.date between :startDate and :endDate")
    Page<ListingAvailability> findByListingAndDateRange(
            @Param("listingId") String listingId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT la FROM ListingAvailability la WHERE la.listing.id = :listingId AND la.status = :status")
    Page<ListingAvailability> findByListingAndStatus(
            @Param("listingId") String listingId,
            @Param("status") String status,
            Pageable pageable
    );
}
