package com.project.airbnb.repositories;

import com.project.airbnb.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByObjectId(String id);
}
