package com.project.airbnb.repositories;

import com.project.airbnb.models.Location.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByNameContainingIgnoreCase(String name);
}
