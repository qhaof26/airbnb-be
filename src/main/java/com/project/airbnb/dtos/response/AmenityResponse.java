package com.project.airbnb.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmenityResponse {
    private String id;
    private String amenityName;

}
