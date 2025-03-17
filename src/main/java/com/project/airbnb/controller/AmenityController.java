package com.project.airbnb.controller;

import com.project.airbnb.dto.request.AmenityRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.AmenityResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.service.Amenity.AmenityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/amenities")
@Tag(name = "Amenity")
public class AmenityController {
    private final AmenityService amenityService;

    @Operation(summary = "Get amenity detail", description = "Send a request via this API to get amenity information")
    @GetMapping("/{amenityId}")
    public APIResponse<AmenityResponse> getCategoryById(@PathVariable Long amenityId){
        return APIResponse.<AmenityResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get amenity by id successfully")
                .data(amenityService.getAmenityById(amenityId))
                .build();
    }

    @Operation(summary = "Get list of amenity per pageNo", description = "Send a request via this API to get amenity list by pageNo and pageSize")
    @GetMapping
    public APIResponse<PageResponse<List<AmenityResponse>>> getAllCategories(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<AmenityResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all amenities successfully")
                .data(amenityService.getAllAmenities(pageNo, pageSize))
                .build();
    }

    @Operation(method = "POST", summary = "Add new amenity", description = "Send a request via this API to create new amenity")
    @PostMapping
    public APIResponse<AmenityResponse> createCategory(@RequestBody AmenityRequest request){
        return APIResponse.<AmenityResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created amenity successfully")
                .data(amenityService.createAmenity(request))
                .build();
    }

    @Operation(method = "PATCH", summary = "Change name of amenity", description = "Send a request via this API to change name of amenity")
    @PatchMapping("/{amenityId}")
    public APIResponse<AmenityResponse> updateCategory(@RequestBody AmenityRequest request, @PathVariable Long amenityId){
        return APIResponse.<AmenityResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated amenity successfully")
                .data(amenityService.updateAmenity(request, amenityId))
                .build();
    }

    @Operation(summary = "Delete amenity permanently", description = "Send a request via this API to delete amenity permanently")
    @DeleteMapping("/{amenityId}")
    public APIResponse<Void> deleteCategory(@PathVariable Long amenityId){
        amenityService.deleteAmenity(amenityId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted amenity successfully")
                .build();
    }

}
