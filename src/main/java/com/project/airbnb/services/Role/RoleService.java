package com.project.airbnb.services.Role;

import com.project.airbnb.dto.request.RoleCreationRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.RoleResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.RoleMapper;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.UserHasRole;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserHasRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final UserHasRoleRepository userHasRoleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse fetchRoleByName(String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public PageResponse<List<RoleResponse>> fetchAllRole(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Role> pageRole = roleRepository.findAll(pageable);
        List<RoleResponse> roleResponses = pageRole.stream().map(roleMapper::toRoleResponse).toList();

        return PageResponse.<List<RoleResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(pageRole.getTotalPages())
                .totalElement(pageRole.getTotalElements())
                .data(roleResponses)
                .build();
    }

    @Override
    @Transactional
    public RoleResponse createRole(RoleCreationRequest request) {
        if(roleRepository.existsByRoleName(request.getRoleName())) throw new AppException(ErrorCode.ROLE_EXISTED);
        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    @Transactional
    public boolean removeRole(String roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        List<String> userHasRoleId = userHasRoleRepository.findAllByRoleId(roleId).stream().map(UserHasRole::getId).toList();
        if(!userHasRoleId.isEmpty()) userHasRoleRepository.deleteAllById(userHasRoleId);
        roleRepository.delete(role);
        return true;
    }
}
