package com.project.airbnb.services.ListingAvailability;

import com.project.airbnb.dtos.request.ListingAvailabilityCreationRequest;
import com.project.airbnb.dtos.request.ListingAvailabilityUpdateRequest;
import com.project.airbnb.dtos.response.ListingAvailabilityResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IListingAvailabilityService {
    ListingAvailabilityResponse getListingAvailability(String id); //GUEST, HOST, ADMIN
    PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByListing(String listingId, int pageNo, int pageSize); //GUEST, HOST, ADMIN
    PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByTime(String listingId, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize); //GUEST, HOST, ADMIN
    ListingAvailabilityResponse createListingAvailability(ListingAvailabilityCreationRequest request); //HOST, ADMIN
    ListingAvailabilityResponse updateListingAvailability(String id, ListingAvailabilityUpdateRequest request); //HOST, ADMIN
    void createListingAvailabilityForMonth(String listingId, int year, int month, BigDecimal price);
}
