package com.project.airbnb.controller;

import com.project.airbnb.dto.request.CategoryRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.CategoryResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.service.Category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
@Tag(name = "Category")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Get category detail", description = "Send a request via this API to get category information")
    @GetMapping("/{categoryId}")
    public APIResponse<CategoryResponse> getCategoryById(@PathVariable Long categoryId){
        return APIResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get category by id successfully")
                .data(categoryService.getCategoryById(categoryId))
                .build();
    }

    @Operation(summary = "Get list of category per pageNo", description = "Send a request via this API to get category list by pageNo and pageSize")
    @GetMapping
    public APIResponse<PageResponse<List<CategoryResponse>>> getAllCategories(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<CategoryResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all categories  successfully")
                .data(categoryService.getAllCategories(pageNo, pageSize))
                .build();
    }

    @Operation(summary = "Add new category", description = "Send a request via this API to create new category")
    @PostMapping
    public APIResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
        return APIResponse.<CategoryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created category successfully")
                .data(categoryService.createCategory(request))
                .build();
    }

    @Operation(summary = "Change name of category", description = "Send a request via this API to change name of category")
    @PatchMapping("/{categoryId}")
    public APIResponse<CategoryResponse> updateCategory(@RequestBody CategoryRequest request, @PathVariable Long categoryId){
        return APIResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated category successfully")
                .data(categoryService.updateCategory(request, categoryId))
                .build();
    }

    @Operation(summary = "Delete category permanently", description = "Send a request via this API to delete categorys permanently")
    @DeleteMapping("/{categoryId}")
    public APIResponse<Void> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted category successfully")
                .build();
    }

}
