package com.project.airbnb.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WardResponse {
    private long id;
    private String wardName;
    private String districtName;
    private String provinceName;
}
