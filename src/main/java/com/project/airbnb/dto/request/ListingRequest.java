package com.project.airbnb.dto.request;

import lombok.Data;

@Data
public class ListingRequest {
    private String address;
    private double latitude;
    private double longitude;
}
