package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.UserResponse;
import com.project.airbnb.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserMapper {

    public UserResponse toUserResponse(User user){
        String roleName = null;
        if(user.getRole() != null){
            roleName = user.getRole().getRoleName();
        }
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(roleName)
                .build();
    }
}
