package com.project.airbnb.services.User;

import com.project.airbnb.constants.PredefinedRole;
import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.models.UserHasRole;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserHasRoleRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final UserHasRoleRepository userHasRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse fetchUserById(String userId) {
        User user = userRepository.findUserActive(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    public PageResponse<List<UserResponse>> fetchAllUser(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<User> pageUser = userRepository.findAll(pageable);
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

        String password = passwordEncoder.encode(request.getPassword());
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(password)
                .email(request.getEmail())
                .isActive(true)
                .build();
        userRepository.save(newUser);

        Role guestRole = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        UserHasRole userHasRole = UserHasRole.builder()
                .role(guestRole)
                .user(newUser)
                .build();
        userHasRoleRepository.save(userHasRole);

        return userMapper.toUserResponse(newUser);
    }

    @Override
    public boolean removeUser(String userId) {
        User user = userRepository.findUserActive(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.isActive()) user.setActive(false);
        userRepository.save(user);
        return true;
    }
}
