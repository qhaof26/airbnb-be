package com.project.airbnb.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmenityResponse {
    private Long id;
    private String amenityName;
}
