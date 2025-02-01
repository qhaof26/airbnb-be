package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.RoleCreationRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.RoleResponse;
import com.project.airbnb.services.Role.RoleService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/{roleName}")
    public APIResponse<RoleResponse> fetchRoleById(@PathVariable String roleName) {
        return APIResponse.<RoleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Fetch role successful")
                .data(roleService.getRoleByName(roleName))
                .build();
    }

    @GetMapping()
    public APIResponse<PageResponse<List<RoleResponse>>> fetchAllRole(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return APIResponse.<PageResponse<List<RoleResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch all role successful")
                .data(roleService.getAllRole(pageNo, pageSize))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return APIResponse.<RoleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created role successful")
                .data(roleService.createRole(request))
                .build();
    }

    @DeleteMapping("/{roleId}")
    public APIResponse<Boolean> removeRole(@PathVariable Long roleId) {
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted role")
                .data(roleService.removeRole(roleId))
                .build();
    }
}
