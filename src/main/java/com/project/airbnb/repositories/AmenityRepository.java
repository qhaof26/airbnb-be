package com.project.airbnb.repositories;

import com.project.airbnb.models.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, String> {
    boolean existsByAmenityName(String amenityName);
}
