package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.models.Listing;
import com.project.airbnb.utils.validator.DateConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
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
    private ListingStatus status;
    @NotNull
    private Listing listing;
}
