package com.project.airbnb.controller;

import com.project.airbnb.dto.request.RoleCreationRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.RoleResponse;
import com.project.airbnb.service.Role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
@Tag(name = "Role")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Get role by role name", description = "Send a request via this API to get role by role name")
    @GetMapping("/{roleName}")
    public APIResponse<RoleResponse> fetchRoleById(@PathVariable String roleName) {
        return APIResponse.<RoleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Fetch role successful")
                .data(roleService.getRoleByName(roleName))
                .build();
    }

    @Operation(summary = "Get list of role", description = "Send a request via this API to get list of role")
    @GetMapping
    public APIResponse<PageResponse<List<RoleResponse>>> fetchAllRole(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return APIResponse.<PageResponse<List<RoleResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch all role successful")
                .data(roleService.getAllRole(pageNo, pageSize))
                .build();
    }

    @Operation(summary = "Create new role", description = "Create new role")
    @PostMapping
    public APIResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return APIResponse.<RoleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created role successful")
                .data(roleService.createRole(request))
                .build();
    }

    @Operation(summary = "Delete role permanently", description = "Send a request via this API to delete role permanently")
    @DeleteMapping("/{roleId}")
    public APIResponse<Boolean> removeRole(@PathVariable Long roleId) {
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted role")
                .data(roleService.removeRole(roleId))
                .build();
    }
}
