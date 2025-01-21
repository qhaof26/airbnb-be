package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.*;
import com.project.airbnb.enums.ImageType;
import com.project.airbnb.services.Listing.ListingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/listings")
public class ListingController {
    private final ListingService listingService;

    @GetMapping("/search")
    public APIResponse<PageResponse<List<ListingResponse>>> filterListings(@RequestParam Map<Object, String> filters){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Search listings")
                .data(listingService.filterListings(filters))
                .build();
    }

    @GetMapping("/{listingId}")
    public APIResponse<ListingResponseDetail> getListingById(@PathVariable String listingId){
        return APIResponse.<ListingResponseDetail>builder()
                .status(HttpStatus.OK.value())
                .message("Get listing by id")
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
                .message("Get all listings")
                .data(listingService.getAllListings(pageNo, pageSize))
                .build();
    }

    @GetMapping("/host")
    public APIResponse<PageResponse<List<ListingResponse>>> getAllListingsOfHost(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all listings of host")
                .data(listingService.getAllListingsOfHost(pageNo, pageSize))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<ListingResponseDetail> createListing(@RequestBody ListingCreationRequest request){
        return APIResponse.<ListingResponseDetail>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created listing successful")
                .data(listingService.createListing(request))
                .build();
    }

    @PostMapping("/images")
    public APIResponse<CloudinaryResponse> uploadImage(
            @RequestParam("id") String id,
            @RequestParam("image")MultipartFile image
            ) throws IOException {
        return APIResponse.<CloudinaryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Uploaded image listing successful")
                .data(listingService.uploadImage(id, image))
                .build();
    }

    @PatchMapping("/update/{listingId}")
    public APIResponse<ListingResponseDetail> updateListing(@RequestBody ListingUpdateRequest request, @PathVariable String listingId){
        return APIResponse.<ListingResponseDetail>builder()
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
