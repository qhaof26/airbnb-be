package com.project.airbnb.dtos.request;

import com.project.airbnb.models.Listing;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreationRequest {
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Listing listing;
    private Short numGuests;
    private String note;
}
