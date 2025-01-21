package com.project.airbnb.dtos.request;

import com.project.airbnb.models.Listing;
import com.project.airbnb.utils.validator.DateConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreationRequest {
    @DateConstraint
    private LocalDate checkinDate;
    @DateConstraint
    private LocalDate checkoutDate;
    @NotNull
    private Listing listing;
    @Min(1)
    private int numGuests;
    private String note;
}
