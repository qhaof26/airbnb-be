package com.project.airbnb.repositories;

import com.project.airbnb.models.Listing;
import com.project.airbnb.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface ListingRepository extends JpaRepository<Listing, String> , JpaSpecificationExecutor<Listing> {
    Page<Listing> findByHost(User user, Pageable pageable);

}
