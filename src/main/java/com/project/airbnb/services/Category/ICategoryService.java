package com.project.airbnb.services.Category;

import com.project.airbnb.dto.request.CategoryRequest;
import com.project.airbnb.dto.response.CategoryResponse;
import com.project.airbnb.dto.response.PageResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse getCategoryById(String categoryId);
    PageResponse<List<CategoryResponse>> getAllCategories(int pageNo, int pageSize);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(CategoryRequest request, String categoryId);
    void deleteCategory(String categoryId);
}
