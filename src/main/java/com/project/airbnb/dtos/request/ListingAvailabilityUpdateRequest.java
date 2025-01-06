package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.models.Listing;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingAvailabilityUpdateRequest {
    private ListingStatus status;
    private BigDecimal price;
}
