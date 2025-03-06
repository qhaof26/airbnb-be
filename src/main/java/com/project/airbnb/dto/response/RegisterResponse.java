package com.project.airbnb.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Boolean status;
}
