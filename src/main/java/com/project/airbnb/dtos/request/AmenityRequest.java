package com.project.airbnb.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmenityRequest {
    @NotBlank
    private String amenityName;
}
