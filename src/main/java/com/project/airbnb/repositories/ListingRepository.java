package com.project.airbnb.repositories;

import com.project.airbnb.models.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, String> {
}
