package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.AmenityResponse;
import com.project.airbnb.models.Amenity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    AmenityResponse toAmenityResponse(Amenity amenity);
}
