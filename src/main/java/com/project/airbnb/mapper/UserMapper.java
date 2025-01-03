package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.models.UserHasRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user){
        Set<Role> roles = user.getRoles().stream().map(UserHasRole::getRole).collect(Collectors.toSet());
        Set<String> roleNames = roles.stream().map(Role::getRoleName).collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(roleNames)
                .build();
    }
}
