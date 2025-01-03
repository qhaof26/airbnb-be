package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.models.UserHasRole;
import com.project.airbnb.repositories.UserHasRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserMapper {
    private final UserHasRoleRepository userHasRoleRepository;

    public UserResponse toUserResponse(User user){

//        Set<Role> roles = user.getRoles().stream().map(UserHasRole::getRole).collect(Collectors.toSet());
//        Set<String> roleNames = roles.stream().map(Role::getRoleName).collect(Collectors.toSet());

        Set<String> roleNames = userHasRoleRepository.findRolesByUserId(user.getId());
        if(roleNames.isEmpty()){
            log.error("UserHasRole is null");
            roleNames = null;
        }
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
