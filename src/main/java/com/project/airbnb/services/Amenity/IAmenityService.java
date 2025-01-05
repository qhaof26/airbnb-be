package com.project.airbnb.services.Amenity;

import com.project.airbnb.dto.request.AmenityRequest;
import com.project.airbnb.dto.response.AmenityResponse;
import com.project.airbnb.dto.response.CategoryResponse;
import com.project.airbnb.dto.response.PageResponse;

import java.util.List;

public interface IAmenityService {
    AmenityResponse getAmenityById(String amenityId);
    PageResponse<List<AmenityResponse>> getAllAmenities(int pageNo, int pageSize);
    AmenityResponse createAmenity(AmenityRequest request);
    AmenityResponse updateAmenity(AmenityRequest request, String amenityId);
    void deleteAmenity(String amenityId);
}
