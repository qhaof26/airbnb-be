package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.UserResponse;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserMapper {

    public UserResponse toUserResponse(User user){
        Set<String> roleName = null;
        if(user.getRoles() != null){
            roleName = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
        }
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(roleName)
                .build();
    }
}
