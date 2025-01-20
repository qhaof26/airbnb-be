package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.models.Listing;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingAvailabilityUpdateRequest {
    @NotNull
    private ListingStatus status;
}
