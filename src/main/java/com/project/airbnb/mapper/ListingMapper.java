package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.ListingDTO;
import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.ListingResponseDetail;
import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.models.*;
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
        String image = null;
        if(!listing.getImages().isEmpty()){
            image = listing.getImages().get(0);
        }
        return ListingResponse.builder()
                .id(listing.getId())
                .listingName(listing.getListingName())
                .nightlyPrice(listing.getNightlyPrice())
                .address(listing.getAddress())
                .status(listing.getStatus())
                .image(image)
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

        List<String> images = null;
        if(!listing.getImages().isEmpty()){
            images = listing.getImages();
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

    public ListingResponse convertDTO(ListingDTO listingDTO){
        return ListingResponse.builder()
                .id(listingDTO.getId())
                .listingName(listingDTO.getListingName())
                .nightlyPrice(listingDTO.getNightlyPrice())
                .address(listingDTO.getAddress())
                .status(ListingStatus.fromCode(listingDTO.getStatus()))
                .image(listingDTO.getImage())
                .build();
    }
}
