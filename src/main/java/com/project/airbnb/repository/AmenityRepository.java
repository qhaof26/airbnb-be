package com.project.airbnb.repository;

import com.project.airbnb.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    boolean existsByAmenityName(String amenityName);
}
