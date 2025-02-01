package com.project.airbnb.dtos.response;

import java.math.BigDecimal;

public interface ListingDTO {
    String getId();
    String getListingName();
    BigDecimal getNightlyPrice();
    String getAddress();
    Integer getStatus();
    String getImage();
}
