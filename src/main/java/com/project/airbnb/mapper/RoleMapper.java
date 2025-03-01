package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.RoleResponse;
import com.project.airbnb.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleResponse toRoleResponse(Role role){
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    };

}
