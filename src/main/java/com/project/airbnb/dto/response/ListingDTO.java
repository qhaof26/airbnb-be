package com.project.airbnb.dto.response;

import java.math.BigDecimal;

public interface ListingDTO {
    String getId();
    String getListingName();
    BigDecimal getNightlyPrice();
    String getAddress();
    Integer getStatus();
    String getImage();
}
