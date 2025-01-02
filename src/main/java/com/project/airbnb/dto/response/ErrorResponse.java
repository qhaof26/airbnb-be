package com.project.airbnb.dto.response;

import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private Instant timestamp;
    private HttpStatusCode statusCode;
    private String message;
}
