package com.project.airbnb.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {
    private String name;
    private Long code;
    private String division_type;
    private String codename;
    private int phone_code;
    private List<DistrictResponse> districts;
}
