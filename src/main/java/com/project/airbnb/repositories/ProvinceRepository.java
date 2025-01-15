package com.project.airbnb.repositories;

import com.project.airbnb.models.Location.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    boolean existsById(Long id);

}
