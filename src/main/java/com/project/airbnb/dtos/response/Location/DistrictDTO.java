package com.project.airbnb.dtos.response.Location;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDTO {
    private Long code;
    private String name;
    private String division_type;
    private String codename;
    private long province_code;
    private List<WardDTO> wards;
}
