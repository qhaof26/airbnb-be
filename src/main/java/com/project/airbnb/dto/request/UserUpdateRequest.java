package com.project.airbnb.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String id;
    private String firstName;
    private String lastName;
}
