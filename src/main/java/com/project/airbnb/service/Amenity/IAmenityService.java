package com.project.airbnb.service.Amenity;

import com.project.airbnb.dto.request.AmenityRequest;
import com.project.airbnb.dto.response.AmenityResponse;
import com.project.airbnb.dto.response.PageResponse;

import java.util.List;

public interface IAmenityService {
    AmenityResponse getAmenityById(Long amenityId); //GUEST, HOST, ADMIN
    PageResponse<List<AmenityResponse>> getAllAmenities(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    AmenityResponse createAmenity(AmenityRequest request); //HOST, ADMIN
    AmenityResponse updateAmenity(AmenityRequest request, Long amenityId); //HOST, ADMIN
    void deleteAmenity(Long amenityId); //ADMIN
}
