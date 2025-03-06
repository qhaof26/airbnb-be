package com.project.airbnb.service.Role;

import com.project.airbnb.dto.request.RoleCreationRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse getRoleByName(String roleName); // ADMIN

    PageResponse<List<RoleResponse>> getAllRole(int pageNo, int pageSize); // ADMIN

    RoleResponse createRole(RoleCreationRequest request); // ADMIN

    boolean removeRole(Long roleId); // ADMIN
}
