package com.project.airbnb.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
}
