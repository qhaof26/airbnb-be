package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.models.Amenity;
import com.project.airbnb.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingUpdateRequest {
    @NotBlank
    private String listingName;
    @NotNull
    @Min(100000)
    private BigDecimal nightlyPrice;
    private int numBeds;
    private int numBedrooms;
    private int numBathrooms;
    @NotNull
    @Min(1)
    private int numGuests;
    private String description;
    @NotBlank
    private String address;
    @NotNull
    private ListingStatus status;
    @NotNull
    private Category category;
    private Set<Amenity> amenities;
}
