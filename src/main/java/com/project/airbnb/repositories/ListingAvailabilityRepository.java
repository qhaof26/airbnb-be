package com.project.airbnb.repositories;

import com.project.airbnb.enums.ListingAvailabilityStatus;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.ListingAvailability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ListingAvailabilityRepository
                extends JpaRepository<ListingAvailability, Long>, JpaSpecificationExecutor<ListingAvailability> {
        Page<ListingAvailability> findByListing(Listing listing, Pageable pageable);

        boolean existsByDateAndListingId(LocalDate date, String listingId);

        boolean existsByListing(Listing listing);

        @Query("select la from ListingAvailability la where la.listing.id = :listingId and la.date between :startDate and :endDate and la.status = :status")
        List<ListingAvailability> checkListingAndDateRangeAndStatus(
                        @Param("listingId") String listingId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("status") ListingAvailabilityStatus status);

        @Query("select la from ListingAvailability la where la.listing.id = :listingId and la.date between :startDate and :endDate")
        Page<ListingAvailability> findByListingAndDateRange(
                        @Param("listingId") String listingId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        Pageable pageable);

        @Query("select la.listing.id from ListingAvailability la " +
                        "where la.date between :startDate and :endDate " +
                        "and la.status = :status " +
                        "group by la.listing.id " +
                        "having count(la.date) = :totalDays")
        List<String> findAvailableListingIds(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("totalDays") long totalDays,
                        @Param("status") ListingAvailabilityStatus status);

}
