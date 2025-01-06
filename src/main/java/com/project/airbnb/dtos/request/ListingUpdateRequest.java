package com.project.airbnb.dtos.request;

import com.project.airbnb.models.Amenity;
import com.project.airbnb.models.Category;
import com.project.airbnb.models.Ward;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingUpdateRequest {
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
