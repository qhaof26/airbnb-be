package com.project.airbnb.services.Amenity;

import com.project.airbnb.dtos.request.AmenityRequest;
import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.util.List;

public interface IAmenityService {
    AmenityResponse getAmenityById(Long amenityId); //GUEST, HOST, ADMIN
    PageResponse<List<AmenityResponse>> getAllAmenities(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    AmenityResponse createAmenity(AmenityRequest request); //HOST, ADMIN
    AmenityResponse updateAmenity(AmenityRequest request, Long amenityId); //HOST, ADMIN
    void deleteAmenity(Long amenityId); //ADMIN
}
