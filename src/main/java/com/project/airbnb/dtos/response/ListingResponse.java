package com.project.airbnb.dtos.response;

import com.project.airbnb.enums.ListingStatus;
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
    private ListingStatus status;
    private String image;

}
