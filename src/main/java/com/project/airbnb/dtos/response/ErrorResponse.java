package com.project.airbnb.dtos.response;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private Instant timestamp;
    private int statusCode;
    private String message;
}
