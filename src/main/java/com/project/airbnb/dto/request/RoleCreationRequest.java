package com.project.airbnb.dto.request;

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
