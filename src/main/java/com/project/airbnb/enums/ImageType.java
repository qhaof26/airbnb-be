package com.project.airbnb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
    BASIC(0),
    AVATAR(1);
    private final int value;
}
