package com.project.airbnb.services.Role;

import com.project.airbnb.dtos.request.RoleCreationRequest;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse getRoleByName(String roleName); // ADMIN

    PageResponse<List<RoleResponse>> getAllRole(int pageNo, int pageSize); // ADMIN

    RoleResponse createRole(RoleCreationRequest request); // ADMIN

    boolean removeRole(Long roleId); // ADMIN
}
