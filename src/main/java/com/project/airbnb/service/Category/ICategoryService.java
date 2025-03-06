package com.project.airbnb.service.Category;

import com.project.airbnb.dto.request.CategoryRequest;
import com.project.airbnb.dto.response.CategoryResponse;
import com.project.airbnb.dto.response.PageResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse getCategoryById(Long categoryId); //GUEST, HOST, ADMIN
    PageResponse<List<CategoryResponse>> getAllCategories(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    CategoryResponse createCategory(CategoryRequest request); //ADMIN
    CategoryResponse updateCategory(CategoryRequest request, Long categoryId); //ADMIN
    void deleteCategory(Long categoryId); //ADMIN
}
