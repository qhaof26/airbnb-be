package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
}
