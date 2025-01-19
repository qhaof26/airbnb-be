package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.dtos.response.CategoryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.ListingResponseDetail;
import com.project.airbnb.models.*;
import com.project.airbnb.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListingMapper {
    private final WardMapper wardMapper;
    private final AmenityMapper amenityMapper;
    private final CategoryMapper categoryMapper;
    private final ImageRepository imageRepository;

    public ListingResponse toListingResponse(Listing listing){
        String url = imageRepository.findAvatarListing(listing.getId()).orElse(null);

        return ListingResponse.builder()
                .id(listing.getId())
                .listingName(listing.getListingName())
                .nightlyPrice(listing.getNightlyPrice())
                .address(listing.getAddress())
                .status(listing.getStatus())
                .ward(wardMapper.toWardResponse(listing.getWard()))
                .image(url)
                .build();
    }

    public ListingResponseDetail toListingResponseDetail(Listing listing){
        CategoryResponse category = null;
        if(listing.getCategory() != null){
            category = categoryMapper.toCategoryResponse(listing.getCategory());
        }

        ListingResponseDetail.UserResponse host = null;
        if(listing.getUser() != null){
            host = ListingResponseDetail.UserResponse.builder()
                    .firstName(listing.getUser().getFirstName())
                    .lastName(listing.getUser().getLastName())
                    .email(listing.getUser().getEmail())
                    .build();
        }

        Set<AmenityResponse> amenities = null;
        if(listing.getAmenities() != null){
            amenities = listing.getAmenities().stream().map(amenityMapper::toAmenityResponse).collect(Collectors.toSet());
        }

        Set<String> images = imageRepository.findImagesListing(listing.getId()).orElse(null);
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
                .ward(wardMapper.toWardResponse(listing.getWard()))
                .category(category)
                .host(host)
                .images(images)
                .build();
    }
}
