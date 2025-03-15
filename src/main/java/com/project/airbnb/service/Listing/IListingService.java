package com.project.airbnb.service.Listing;

import com.project.airbnb.dto.request.ListingCreationRequest;
import com.project.airbnb.dto.request.ListingUpdateRequest;
import com.project.airbnb.dto.response.CloudinaryResponse;
import com.project.airbnb.dto.response.ListingResponse;
import com.project.airbnb.dto.response.ListingResponseDetail;
import com.project.airbnb.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IListingService {
    PageResponse<List<ListingResponse>> searchListings(Map<Object, String> filters);

    PageResponse<List<ListingResponse>> filterListings(Map<Object, String> filters);

    ListingResponseDetail getListingById(String listingId); // GUEST, HOST, ADMIN

    PageResponse<List<ListingResponse>> getAllListings(int pageNo, int pageSize); // GUEST, HOST, ADMIN

    PageResponse<List<ListingResponse>> getAllListingsOfHost(int pageNo, int pageSize); // HOST, ADMIN

    ListingResponseDetail createListing(ListingCreationRequest request); // HOST, ADMIN

    CloudinaryResponse uploadImage(String listingId, MultipartFile file) throws IOException; // HOST, ADMIN

    ListingResponseDetail updateListing(String listingId, ListingUpdateRequest request); // HOST, ADMIN

    void deleteListing(String listingId); // HOST, ADMIN
}
