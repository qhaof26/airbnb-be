package com.project.airbnb.dtos.response;

import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.models.Listing;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingAvailabilityResponse {
    private String id;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private ListingStatus status;
    private Listing listing;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Listing{
        private String id;
        private String listingName;
    }
}
