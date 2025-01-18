package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListingMapper {
    private final WardMapper wardMapper;
    private final UserMapper userMapper;
    private final AmenityMapper amenityMapper;
    private final CategoryMapper categoryMapper;

    public ListingResponse toListingResponse(Listing listing){
        CategoryResponse category = null;
        if(listing.getCategory() != null){
            category = categoryMapper.toCategoryResponse(listing.getCategory());
        }

        User user = null;
        if(listing.getUser() != null){
            user = listing.getUser();
        }

        Set<AmenityResponse> amenities = null;
        if(listing.getAmenities() != null){
            amenities = listing.getAmenities().stream().map(amenityMapper::toAmenityResponse).collect(Collectors.toSet());
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
                .status(listing.getStatus())
                .amenities(amenities)
                .ward(wardMapper.toWardResponse(listing.getWard()))
                .category(category)
                .host(userMapper.toUserResponse(Objects.requireNonNull(user)))
                .build();
    }
}
