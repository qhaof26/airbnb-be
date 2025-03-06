package com.project.airbnb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank
    @Size(min = 6)
    private String username;

    @Size(min = 6)
    private String password;
}
