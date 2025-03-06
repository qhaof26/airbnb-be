package com.project.airbnb.dto.response;

import com.project.airbnb.enums.ListingAvailabilityStatus;
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
    private Long id;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private ListingAvailabilityStatus status;
    private BigDecimal price;
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
