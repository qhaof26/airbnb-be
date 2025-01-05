package com.project.airbnb.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WardResponse {
    private Long code;
    private String name;
    private String division_type;
    private String codename;
    private long district_code;
}
