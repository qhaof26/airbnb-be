package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.CategoryRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.services.Category.CategoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public APIResponse<CategoryResponse> getCategoryById(@PathVariable Long categoryId){
        return APIResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get category by id successful")
                .data(categoryService.getCategoryById(categoryId))
                .build();
    }

    @GetMapping()
    public APIResponse<PageResponse<List<CategoryResponse>>> getAllCategories(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<CategoryResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all categories  successful")
                .data(categoryService.getAllCategories(pageNo, pageSize))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
        return APIResponse.<CategoryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created category successful")
                .data(categoryService.createCategory(request))
                .build();
    }

    @PatchMapping("/update/{categoryId}")
    public APIResponse<CategoryResponse> updateCategory(@RequestBody CategoryRequest request, @PathVariable Long categoryId){
        return APIResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated category successful")
                .data(categoryService.updateCategory(request, categoryId))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    public APIResponse<Void> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted category")
                .build();
    }

}
