package com.project.airbnb.repositories;

import com.project.airbnb.models.ListingAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ListingAvailabilityRepository extends JpaRepository<ListingAvailability, String>, JpaSpecificationExecutor<ListingAvailability> {
}
