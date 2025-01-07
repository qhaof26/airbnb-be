package com.project.airbnb.dtos.response.Location;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WardDTO {
    private Long code;
    private String name;
    private String division_type;
    private String codename;
    private long district_code;
}
