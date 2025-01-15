package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.WardResponse;
import com.project.airbnb.models.Location.Ward;
import org.springframework.stereotype.Component;

@Component
public class WardMapper {
    public WardResponse toWardResponse(Ward ward){
        return WardResponse.builder()
                .id(ward.getId())
                .wardName(ward.getName())
                .districtName(ward.getDistrict().getName())
                .provinceName(ward.getDistrict().getProvince().getName())
                .build();
    }
}
