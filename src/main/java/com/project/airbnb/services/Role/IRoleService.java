package com.project.airbnb.services.Role;

import com.project.airbnb.dto.request.RoleCreationRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse getRoleByName(String roleName);
    PageResponse<List<RoleResponse>> getAllRole(int pageNo, int pageSize);
    RoleResponse createRole(RoleCreationRequest request);
    boolean removeRole(String roleId);
}
