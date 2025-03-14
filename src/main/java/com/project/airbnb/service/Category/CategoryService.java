package com.project.airbnb.service.Category;

import com.project.airbnb.dto.request.CategoryRequest;
import com.project.airbnb.dto.response.CategoryResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.exception.AppException;
import com.project.airbnb.exception.ErrorCode;
import com.project.airbnb.mapper.CategoryMapper;
import com.project.airbnb.model.Category;
import com.project.airbnb.repository.CategoryRepository;
import com.project.airbnb.repository.ListingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    private final ListingRepository listingRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
    }

    @Override
    public PageResponse<List<CategoryResponse>> getAllCategories(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryResponse> categoryResponses = categoryPage.getContent().stream().map(categoryMapper::toCategoryResponse).toList();

        return PageResponse.<List<CategoryResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(categoryPage.getTotalPages())
                .totalElement(categoryPage.getTotalElements())
                .data(categoryResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if(categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .build();
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CategoryResponse updateCategory(CategoryRequest request, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        if(categoryRepository.existsByCategoryName(request.getCategoryName())){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        category.setCategoryName(request.getCategoryName());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.getListings().forEach(listing -> listing.setCategory(null));
        listingRepository.saveAll(category.getListings());
        categoryRepository.delete(category);
    }
}
