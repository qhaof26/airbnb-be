package com.project.airbnb.service.ListingAvailability;

import com.project.airbnb.dto.request.ListingAvailabilityCreationRequest;
import com.project.airbnb.dto.request.ListingAvailabilityUpdateRequest;
import com.project.airbnb.dto.response.ListingAvailabilityResponse;
import com.project.airbnb.dto.response.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface IListingAvailabilityService {
    ListingAvailabilityResponse getListingAvailability(Long id); // GUEST, HOST, ADMIN

    PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByListing(String listingId, int pageNo,
            int pageSize); // GUEST, HOST, ADMIN

    PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByTime(String listingId, LocalDate startDate,
            LocalDate endDate, int pageNo, int pageSize); // GUEST, HOST, ADMIN

    ListingAvailabilityResponse createListingAvailability(ListingAvailabilityCreationRequest request); // HOST, ADMIN

    ListingAvailabilityResponse updateListingAvailability(Long id, ListingAvailabilityUpdateRequest request); // HOST,
                                                                                                              // ADMIN

    void createListingAvailabilityForMonth(String listingId, int year, int month);
}
