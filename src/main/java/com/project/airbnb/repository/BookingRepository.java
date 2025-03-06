package com.project.airbnb.repository;

import com.project.airbnb.enums.BookingStatus;
import com.project.airbnb.model.Booking;
import com.project.airbnb.model.Listing;
import com.project.airbnb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, String>, JpaSpecificationExecutor<Booking> {

    Page<Booking> findAllByUser(User user, Pageable pageable);

    @Query("select b from Booking b " +
            "join Listing l on l.id = b.listing.id " +
            "join User u on u.id = l.host.id " +
            "where u.id = :userId")
    Page<Booking> findAllByHost(Long userId, Pageable pageable);

    boolean existsByListingAndUserAndStatus(Listing listing, User user, BookingStatus status);
}
