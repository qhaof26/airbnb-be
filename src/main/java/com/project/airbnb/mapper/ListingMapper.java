package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.models.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ListingMapper {
    public ListingResponse toListingResponse(Listing listing){
        Ward ward = null;
        if(listing.getWard() != null){
            ward = listing.getWard();
        }

        Category category = null;
        if(listing.getCategory() != null){
            category = listing.getCategory();
        }

        User user = null;
        if(listing.getUser() != null){
            user = listing.getUser();
        }

        Set<Amenity> amenities = null;
        if(listing.getAmenities() != null){
            amenities = listing.getAmenities();
        }
        return ListingResponse.builder()
                .id(listing.getId())
                .nightlyPrice(listing.getNightlyPrice())
                .listingName(listing.getListingName())
                .numGuests(listing.getNumGuests())
                .numBeds(listing.getNumBeds())
                .numBathrooms(listing.getNumBathrooms())
                .numBedrooms(listing.getNumBedrooms())
                .address(listing.getAddress())
                .description(listing.getDescription())
                .amenities(amenities)
                .ward(ward)
                .category(category)
                .user(user)
                .build();
    };
}
