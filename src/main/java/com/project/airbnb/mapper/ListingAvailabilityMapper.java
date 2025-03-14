package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.ListingAvailabilityResponse;
import com.project.airbnb.model.ListingAvailability;
import org.springframework.stereotype.Component;

@Component
public class ListingAvailabilityMapper {
    public ListingAvailabilityResponse toListingAvailabilityResponse(ListingAvailability listingAvailability){
        ListingAvailabilityResponse.Listing listing = new ListingAvailabilityResponse.Listing();
        listing.setId(listingAvailability.getListing().getId());
        listing.setListingName(listingAvailability.getListing().getListingName());
        return ListingAvailabilityResponse.builder()
                .id(listingAvailability.getId())
                .date(listingAvailability.getDate())
                .status(listingAvailability.getStatus())
                .price(listingAvailability.getPrice())
                .listing(listing)
                .build();
    };
}
