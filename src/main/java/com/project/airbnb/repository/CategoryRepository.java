package com.project.airbnb.repository;

import com.project.airbnb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryName(String categoryName);
}
