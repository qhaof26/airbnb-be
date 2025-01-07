package com.project.airbnb.services.Listing;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.util.List;

public interface IListingService {
    ListingResponse getListingById(String listingId);
    PageResponse<List<ListingResponse>> getAllListings(int pageNo, int pageSize);
    ListingResponse createListing(ListingCreationRequest request);
    ListingResponse updateListing(String listingId, ListingUpdateRequest request);
    void deleteListing(String listingId);
}
