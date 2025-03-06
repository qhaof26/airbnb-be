package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.CategoryResponse;
import com.project.airbnb.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
}
