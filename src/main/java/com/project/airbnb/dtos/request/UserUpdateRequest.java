package com.project.airbnb.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
