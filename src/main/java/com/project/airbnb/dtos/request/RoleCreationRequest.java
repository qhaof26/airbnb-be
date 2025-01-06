package com.project.airbnb.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreationRequest {
    private String roleName;
    private String description;
}
