package com.project.airbnb.repositories;

import com.project.airbnb.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    boolean existsById(Long id);

}
