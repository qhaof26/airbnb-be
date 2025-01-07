package com.project.airbnb.repositories;

import com.project.airbnb.models.Booking;
import com.project.airbnb.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, String>, JpaSpecificationExecutor<Booking> {

    Page<Booking> findAllByUser(User user, Pageable pageable);

    @Query("select b from Booking b " +
            "join Listing l on l.id = b.listing.id " +
            "join User u on u.id = l.user.id " +
            "where u.id = :userId")
    Page<Booking> findAllByHost(String userId, Pageable pageable);
}
