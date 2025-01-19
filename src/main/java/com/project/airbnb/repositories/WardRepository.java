package com.project.airbnb.repositories;

import com.project.airbnb.models.Location.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByNameContainingIgnoreCase(String name);
}
