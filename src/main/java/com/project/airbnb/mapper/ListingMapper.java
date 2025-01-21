package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.ListingResponseDetail;
import com.project.airbnb.models.*;
import com.project.airbnb.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListingMapper {
    private final AmenityMapper amenityMapper;
    private final CategoryMapper categoryMapper;

    public ListingResponse toListingResponse(Listing listing){

        return ListingResponse.builder()
                .id(listing.getId())
                .listingName(listing.getListingName())
                .nightlyPrice(listing.getNightlyPrice())
                .address(listing.getAddress())
                .status(listing.getStatus())
                .image(listing.getImages().get(0))
                .build();
    }

    public ListingResponseDetail toListingResponseDetail(Listing listing){
        CategoryResponse category = null;
        if(listing.getCategory() != null){
            category = categoryMapper.toCategoryResponse(listing.getCategory());
        }

        ListingResponseDetail.UserResponse host = null;
        if(listing.getHost() != null){
            host = ListingResponseDetail.UserResponse.builder()
                    .firstName(listing.getHost().getFirstName())
                    .lastName(listing.getHost().getLastName())
                    .email(listing.getHost().getEmail())
                    .build();
        }

        Set<AmenityResponse> amenities = null;
        if(listing.getAmenities() != null){
            amenities = listing.getAmenities().stream().map(amenityMapper::toAmenityResponse).collect(Collectors.toSet());
        }

        List<String> images = listing.getImages();
        if(images.isEmpty()){
            images = null;
        }
        return ListingResponseDetail.builder()
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
                .category(category)
                .host(host)
                .images(images)
                .build();
    }
}
