package com.project.airbnb.controller;

import com.project.airbnb.dto.request.AmenityRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.AmenityResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.service.Amenity.AmenityService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/amenities")
public class AmenityController {
    private final AmenityService amenityService;

    @GetMapping("/{amenityId}")
    public APIResponse<AmenityResponse> getCategoryById(@PathVariable Long amenityId){
        return APIResponse.<AmenityResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get amenity by id successful")
                .data(amenityService.getAmenityById(amenityId))
                .build();
    }

    @GetMapping
    public APIResponse<PageResponse<List<AmenityResponse>>> getAllCategories(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<AmenityResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all amenities successful")
                .data(amenityService.getAllAmenities(pageNo, pageSize))
                .build();
    }

    @PostMapping
    public APIResponse<AmenityResponse> createCategory(@RequestBody AmenityRequest request){
        return APIResponse.<AmenityResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created amenity successful")
                .data(amenityService.createAmenity(request))
                .build();
    }

    @PatchMapping("/{amenityId}")
    public APIResponse<AmenityResponse> updateCategory(@RequestBody AmenityRequest request, @PathVariable Long amenityId){
        return APIResponse.<AmenityResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated amenity successful")
                .data(amenityService.updateAmenity(request, amenityId))
                .build();
    }

    @DeleteMapping("/{amenityId}")
    public APIResponse<Void> deleteCategory(@PathVariable Long amenityId){
        amenityService.deleteAmenity(amenityId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted amenity")
                .build();
    }

}
