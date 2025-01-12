package com.project.airbnb.services.User;

import com.project.airbnb.constants.PredefinedRole;
import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.UserResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findUserActive(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<List<UserResponse>> getAllUserActive(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<User> pageUser = userRepository.findAllUserActive(pageable);
        List<UserResponse> userResponses = pageUser.getContent().stream().map(userMapper::toUserResponse).toList();

        return PageResponse.<List<UserResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(pageUser.getTotalPages())
                .totalElement(pageUser.getTotalElements())
                .data(userResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<List<UserResponse>> getAllUserBlock(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<User> pageUser = userRepository.findAllUserBlock(pageable);
        List<UserResponse> userResponses = pageUser.getContent().stream().map(userMapper::toUserResponse).toList();

        return PageResponse.<List<UserResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(pageUser.getTotalPages())
                .totalElement(pageUser.getTotalElements())
                .data(userResponses)
                .build();
    }

    @Override
    @Transactional
    public UserResponse createNewUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTED);
        if(userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        Role role = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        String password = passwordEncoder.encode(request.getPassword());
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(password)
                .email(request.getEmail())
                .roles(roles)
                .build();
        userRepository.save(newUser);

        return userMapper.toUserResponse(newUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse changeStatus(String userId, Boolean status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus(status);
        if(user.getRoles().isEmpty()){
            Role role = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            log.info("Inside method find role");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public boolean removeUser(String userId) {
        User user = userRepository.findUserActive(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.getStatus()) user.setStatus(Boolean.FALSE);
        user.setRoles(null);
        userRepository.save(user);

        return true;
    }
}
