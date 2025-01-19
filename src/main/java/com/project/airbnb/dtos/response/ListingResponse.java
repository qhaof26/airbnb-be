package com.project.airbnb.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingResponse {
    private String id;
    private String listingName;
    private BigDecimal nightlyPrice;
    private String address;
    private Boolean status;
    private WardResponse ward;
    private String image;

}
