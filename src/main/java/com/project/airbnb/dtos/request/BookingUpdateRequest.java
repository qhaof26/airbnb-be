package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.BookingStatus;
import com.project.airbnb.models.Listing;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequest {
    private String id;
    private BookingStatus status;
}
