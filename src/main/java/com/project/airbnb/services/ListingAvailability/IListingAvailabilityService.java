package com.project.airbnb.services.ListingAvailability;

import com.project.airbnb.dtos.request.ListingAvailabilityCreationRequest;
import com.project.airbnb.dtos.request.ListingAvailabilityUpdateRequest;
import com.project.airbnb.dtos.response.ListingAvailabilityResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface IListingAvailabilityService {
    ListingAvailabilityResponse getListingAvailability(String id);
    PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByListing(String listingId, int pageNo, int pageSize);
    PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByTime(String listingId, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize);
    ListingAvailabilityResponse createListingAvailability(ListingAvailabilityCreationRequest request);
    ListingAvailabilityResponse updateListingAvailability(String id, ListingAvailabilityUpdateRequest request);
}
