package com.project.airbnb.services.Amenity;

import com.project.airbnb.dtos.request.AmenityRequest;
import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.util.List;

public interface IAmenityService {
    AmenityResponse getAmenityById(String amenityId);
    PageResponse<List<AmenityResponse>> getAllAmenities(int pageNo, int pageSize);
    AmenityResponse createAmenity(AmenityRequest request);
    AmenityResponse updateAmenity(AmenityRequest request, String amenityId);
    void deleteAmenity(String amenityId);
}
