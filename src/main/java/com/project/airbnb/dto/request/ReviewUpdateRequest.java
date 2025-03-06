package com.project.airbnb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequest {
    private Long id;
    private String content;
    private Double star;
    @JsonProperty("listing")
    private String listingId;
}
