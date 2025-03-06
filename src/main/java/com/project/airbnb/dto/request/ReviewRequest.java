package com.project.airbnb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String content;
    private Double star;
//    @JsonProperty("user")
//    private Long userId;
    @JsonProperty("listing")
    private String listingId;
}
