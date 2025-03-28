package com.project.airbnb.dto.request;

import com.project.airbnb.model.*;
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
public class ListingCreationRequest {
    @NotBlank
    private String listingName;
    @NotBlank
    private Double longitude;
    @NotBlank
    private Double latitude;
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
    private Category category;
    private Set<Amenity> amenities;
}
