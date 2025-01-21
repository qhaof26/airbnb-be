package com.project.airbnb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ListingStatus {
    CLOSE(0),
    OPEN(1),
    ;

    private final int value;
}
