package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.services.Listing.ListingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/listings")
public class ListingController {
    private final ListingService listingService;

    @GetMapping("/{listingId}")
    public APIResponse<ListingResponse> getListingById(@PathVariable String listingId){
        return APIResponse.<ListingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get listing by id successful")
                .data(listingService.getListingById(listingId))
                .build();
    }

    @GetMapping()
    public APIResponse<PageResponse<List<ListingResponse>>> getAllListings(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all listings successful")
                .data(listingService.getAllListings(pageNo, pageSize))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<ListingResponse> createListing(@RequestBody ListingCreationRequest request){
        return APIResponse.<ListingResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created listing successful")
                .data(listingService.createListing(request))
                .build();
    }

    @PatchMapping("/update/{listingId}")
    public APIResponse<ListingResponse> updateListing(@RequestBody ListingUpdateRequest request, @PathVariable String listingId){
        return APIResponse.<ListingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated listing successful")
                .data(listingService.updateListing(listingId, request))
                .build();
    }

    @DeleteMapping("/delete/{listingId}")
    public APIResponse<Void> deleteListing(@PathVariable String listingId){
        listingService.deleteListing(listingId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete listing")
                .build();
    }
}
