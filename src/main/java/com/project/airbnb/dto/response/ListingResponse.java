package com.project.airbnb.dto.response;

import com.project.airbnb.enums.ListingStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingResponse {
    private String id;
    private String listingName;
    private BigDecimal nightlyPrice;
    private Float averageRating;
    private String address;
    private ListingStatus status;
    private String image;

    public ListingResponse(ListingDTO listingDTO) {
    }
}
