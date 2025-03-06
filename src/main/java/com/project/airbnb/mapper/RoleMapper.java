package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.RoleResponse;
import com.project.airbnb.model.Role;
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
