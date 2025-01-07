package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.ListingAvailabilityCreationRequest;
import com.project.airbnb.dtos.request.ListingAvailabilityUpdateRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.ListingAvailabilityResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.services.Listing.ListingService;
import com.project.airbnb.services.ListingAvailability.ListingAvailabilityService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/listing-availability")
public class ListingAvailabilityController {
    private final ListingService listingService;
    private final ListingAvailabilityService listingAvailabilityService;

    @GetMapping("/{id}")
    public APIResponse<ListingAvailabilityResponse> getListingAvailabilityById(@PathVariable String id){
        return APIResponse.<ListingAvailabilityResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get listing availability by id")
                .data(listingAvailabilityService.getListingAvailability(id))
                .build();
    }

    @GetMapping("/listing/{listingId}")
    public APIResponse<PageResponse<List<ListingAvailabilityResponse>>> getAllListings(
            @PathVariable String listingId,
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<ListingAvailabilityResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all listing availability by listings")
                .data(listingAvailabilityService.fetchAvailabilityByListing(listingId, pageNo, pageSize))
                .build();
    }

    @GetMapping("/time/{listingId}")
    public APIResponse<PageResponse<List<ListingAvailabilityResponse>>> getAllListingsByTime(
            @PathVariable String listingId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @Min(value = 1)@RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<ListingAvailabilityResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all listing availability by time range")
                .data(listingAvailabilityService.fetchAvailabilityByTime(listingId, startDate, endDate, pageNo, pageSize))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<ListingAvailabilityResponse> createListing(@RequestBody ListingAvailabilityCreationRequest request){
        return APIResponse.<ListingAvailabilityResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created listing availability")
                .data(listingAvailabilityService.createListingAvailability(request))
                .build();
    }

    @PatchMapping("/update/{listingId}")
    public APIResponse<ListingAvailabilityResponse> updateListing(@RequestBody ListingAvailabilityUpdateRequest request, @PathVariable String listingId){
        return APIResponse.<ListingAvailabilityResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated listing availability")
                .data(listingAvailabilityService.updateListingAvailability(listingId, request))
                .build();
    }
}
