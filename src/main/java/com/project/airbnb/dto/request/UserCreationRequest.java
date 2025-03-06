package com.project.airbnb.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 6)
    private String username;
    @NotBlank
    @Size(min = 6)
    private String password;
    @NotBlank
    @Email(message = "Invalid Email format")
    private String email;
}
