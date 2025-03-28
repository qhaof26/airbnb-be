package com.project.airbnb.dto.response;

import com.project.airbnb.enums.ListingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingResponseDetail {
    private String id;
    private String listingName;
    private BigDecimal nightlyPrice;
    private int numBeds;
    private int numBedrooms;
    private int numBathrooms;
    private int numGuests;
    private Float averageRating;
    private String description;
    private String address;
    private ListingStatus status;

    private UserResponse host;
    private CategoryResponse category;
    private Set<AmenityResponse> amenities;
    private List<String> images = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse{
        private String firstName;
        private String lastName;
        private String email;
    }
}
