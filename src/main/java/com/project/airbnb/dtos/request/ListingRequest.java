package com.project.airbnb.dtos.request;

import lombok.Data;

@Data
public class ListingRequest {
    private String address;
    private double latitude;
    private double longitude;
}
