package com.project.airbnb.dtos.response;

import com.project.airbnb.enums.BookingStatus;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Listing listing;
    private BigDecimal nightlyPrice;
    private BigDecimal serviceFee;
    private BigDecimal totalPrice;
    private int numGuests;
    private String note;
    private BookingStatus status;

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
