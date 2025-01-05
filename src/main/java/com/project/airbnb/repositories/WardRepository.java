package com.project.airbnb.repositories;

import com.project.airbnb.models.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, Long> {
}
