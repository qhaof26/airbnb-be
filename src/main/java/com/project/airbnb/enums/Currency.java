package com.project.airbnb.enums;
import lombok.*;

@AllArgsConstructor
@Getter
public enum Currency {

    USD("USD"),
    VND("VND"),
    ;

    private final String value;
}