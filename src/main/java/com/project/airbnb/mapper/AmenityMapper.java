package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.AmenityResponse;
import com.project.airbnb.model.Amenity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    AmenityResponse toAmenityResponse(Amenity amenity);
}
