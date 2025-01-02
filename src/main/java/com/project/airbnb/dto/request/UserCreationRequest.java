package com.project.airbnb.dto.request;

import com.project.airbnb.models.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
}
