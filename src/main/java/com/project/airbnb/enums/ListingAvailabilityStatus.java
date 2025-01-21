package com.project.airbnb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListingAvailabilityStatus {
    AVAILABLE(0),
    HELD(1),
    BOOKED(2),
    SERVED(3)
    ;

    private final int value;
}
