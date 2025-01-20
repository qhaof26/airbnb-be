package com.project.airbnb.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreationRequest {
    @NotBlank
    private String roleName;
    private String description;
}
