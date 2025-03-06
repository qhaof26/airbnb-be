package com.project.airbnb.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Long id;
    private String roleName;
    private String description;
}
