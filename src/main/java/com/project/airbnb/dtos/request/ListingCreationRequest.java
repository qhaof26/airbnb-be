package com.project.airbnb.dtos.request;

import com.project.airbnb.models.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingCreationRequest {
    private String listingName;
    private BigDecimal nightlyPrice;
    private int numBeds;
    private int numBedrooms;
    private int numBathrooms;
    private int numGuests;
    private String description;
    private String address;

    private Ward ward;
    private Category category;
    private Set<Amenity> amenities;
}
