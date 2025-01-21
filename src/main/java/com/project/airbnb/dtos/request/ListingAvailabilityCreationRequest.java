package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.ListingAvailabilityStatus;
import com.project.airbnb.models.Listing;
import com.project.airbnb.utils.validator.DateConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingAvailabilityCreationRequest {
    @DateConstraint
    private LocalDate date;
    @NotNull
    private ListingAvailabilityStatus status;
    @NotNull
    private Listing listing;
}
