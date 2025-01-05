package com.project.airbnb.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponse {
    private Long code;
    private String name;
    private String division_type;
    private String codename;
    private long province_code;
    private List<WardResponse> wards;
}
