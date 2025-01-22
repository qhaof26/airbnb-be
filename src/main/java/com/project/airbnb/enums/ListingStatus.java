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

    public static ListingStatus fromCode(int code) {
        for (ListingStatus status : ListingStatus.values()) {
            if (status.value == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + code);
    }
}
