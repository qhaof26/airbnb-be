package com.project.airbnb.services.Listing;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.util.List;

public interface IListingService {
    ListingResponse getListingById(String listingId); //GUEST, HOST, ADMIN
    PageResponse<List<ListingResponse>> getAllListings(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    ListingResponse createListing(ListingCreationRequest request); //HOST, ADMIN
    ListingResponse updateListing(String listingId, ListingUpdateRequest request); //HOST, ADMIN
    void deleteListing(String listingId); //HOST, ADMIN
}
