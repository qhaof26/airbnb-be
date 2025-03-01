package com.project.airbnb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    DRAFT(0),
    BOOKED(1),
    COMPLETED(2),
    CANCELLED(3)
    ;

    private final int value;
}
