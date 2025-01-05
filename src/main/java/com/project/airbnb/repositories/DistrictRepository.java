package com.project.airbnb.repositories;

import com.project.airbnb.models.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {
}
