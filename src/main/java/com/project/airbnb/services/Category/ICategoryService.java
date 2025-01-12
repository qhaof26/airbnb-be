package com.project.airbnb.services.Category;

import com.project.airbnb.dtos.request.CategoryRequest;
import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse getCategoryById(String categoryId); //GUEST, HOST, ADMIN
    PageResponse<List<CategoryResponse>> getAllCategories(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    CategoryResponse createCategory(CategoryRequest request); //ADMIN
    CategoryResponse updateCategory(CategoryRequest request, String categoryId); //ADMIN
    void deleteCategory(String categoryId); //ADMIN
}
