package com.project.airbnb.controller;

import com.project.airbnb.dto.request.ListingCreationRequest;
import com.project.airbnb.dto.request.ListingUpdateRequest;
import com.project.airbnb.dto.response.*;
import com.project.airbnb.service.Listing.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Room")
public class ListingController {
    private final ListingService listingService;

    @Operation(summary = "Search list of room by checkin, checkout, location, guests", description = "Send a request via this API to get search room by checkin, checkout, location, guests")
    @GetMapping("/search")
    public APIResponse<PageResponse<List<ListingResponse>>> searchListings(@RequestParam Map<Object, String> filters){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Search listings successfully")
                .data(listingService.searchListings(filters))
                .build();
    }

    @Operation(summary = "Filter list of room by Amenity, Category,...", description = "Send a request via this API to filter list of room by Amenity, Category,...")
    @GetMapping("/filters")
    public APIResponse<PageResponse<List<ListingResponse>>> filterListings(@RequestParam Map<Object, String> filters){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Filter listings successfully")
                .data(listingService.filterListings(filters))
                .build();
    }

    @Operation(summary = "Get room detail", description = "Send a request via this API to get room detail")
    @GetMapping("/{listingId}")
    public APIResponse<ListingResponseDetail> getListingById(@PathVariable String listingId){
        return APIResponse.<ListingResponseDetail>builder()
                .status(HttpStatus.OK.value())
                .message("Get listing by id successfully")
                .data(listingService.getListingById(listingId))
                .build();
    }

    @Operation(summary = "Get list of room per pageNo", description = "Send a request via this API to get list of room by pageNo and pageSize")
    @GetMapping
    public APIResponse<PageResponse<List<ListingResponse>>> getAllListings(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all listings successfully")
                .data(listingService.getAllListings(pageNo, pageSize))
                .build();
    }

    @Operation(summary = "Get the list of rooms of the host", description = "Send a request via this API to get the list of rooms of the host")
    @GetMapping("/host")
    public APIResponse<PageResponse<List<ListingResponse>>> getAllListingsOfHost(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<ListingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all listings of host successfully")
                .data(listingService.getAllListingsOfHost(pageNo, pageSize))
                .build();
    }

    @Operation(summary = "Create new room", description = "Send a request via this API to create new room")
    @PostMapping
    public APIResponse<ListingResponseDetail> createListing(@RequestBody ListingCreationRequest request){
        return APIResponse.<ListingResponseDetail>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created listing successfully")
                .data(listingService.createListing(request))
                .build();
    }

    @Operation(summary = "Upload image for room", description = "Send a request via this API to upload image for room")
    @PostMapping("/images")
    public APIResponse<CloudinaryResponse> uploadImage(
            @RequestParam("id") String id,
            @RequestParam("image")MultipartFile image
            ) throws IOException {
        return APIResponse.<CloudinaryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Uploaded image listing successfully")
                .data(listingService.uploadImage(id, image))
                .build();
    }

    @Operation(summary = "Update room", description = "Send a request via this API to update room")
    @PatchMapping("/{listingId}")
    public APIResponse<ListingResponseDetail> updateListing(@RequestBody ListingUpdateRequest request, @PathVariable String listingId){
        return APIResponse.<ListingResponseDetail>builder()
                .status(HttpStatus.OK.value())
                .message("Updated listing successfully")
                .data(listingService.updateListing(listingId, request))
                .build();
    }

    @Operation(summary = "Delete room permanently", description = "Send a request via this API to delete room permanently")
    @DeleteMapping("/{listingId}")
    public APIResponse<Void> deleteListing(@PathVariable String listingId){
        listingService.deleteListing(listingId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete listing successfully")
                .build();
    }
}
