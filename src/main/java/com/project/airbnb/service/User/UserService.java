package com.project.airbnb.service.User;

import com.project.airbnb.constant.PredefinedRole;
import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.CloudinaryResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.exception.AppException;
import com.project.airbnb.exception.ErrorCode;
import com.project.airbnb.model.Role;
import com.project.airbnb.model.User;
import com.project.airbnb.repository.RoleRepository;
import com.project.airbnb.repository.UserRepository;
import com.project.airbnb.mapper.UserMapper;
import com.project.airbnb.repository.specification.UserSpecification;
import com.project.airbnb.service.Cloudinary.CloudinaryService;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findUserActive(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<List<UserResponse>> getAllUserActive(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(handleSort(sortBy)));
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
    public PageResponse<List<UserResponse>> getAllUserBlock(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(handleSort(sortBy)));
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
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<List<UserResponse>> searchUsers(int pageNo, int pageSize,String keyword, String sortBy) {
        Specification<User> spec = UserSpecification.searchByKeyword(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(handleSort(sortBy)));
        Page<User> userPage = userRepository.findAll(spec, pageable);

        List<UserResponse> userResponses = userPage.getContent().stream().map(userMapper::toUserResponse).toList();

        return PageResponse.<List<UserResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(userPage.getTotalPages())
                .totalElement(userPage.getTotalElements())
                .data(userResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<List<UserResponse>> filterUsers(Map<Object, String> filters) {
        int pageNo, pageSize;
        try {
            pageNo = Integer.parseInt(filters.getOrDefault("pageNo", "1"));
            pageSize = Integer.parseInt(filters.getOrDefault("pageSize", "10"));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NumberFormatException pageNo, pageSize");
        }

        String sortBy = filters.get("sortBy");
        String roleName = filters.get("roleName");
        String keyword = filters.get("keyword");
        Boolean status = Optional.ofNullable(filters.get("status"))
                .filter(s -> !s.isEmpty())
                .map(Boolean::parseBoolean)
                .orElse(null);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(handleSort(sortBy)));
        Specification<User> spec = UserSpecification.filterUsers(roleName, status, keyword);
        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<UserResponse> userResponses = userPage.getContent().stream().map(userMapper::toUserResponse).toList();

        return PageResponse.<List<UserResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(userPage.getTotalPages())
                .totalElement(userPage.getTotalElements())
                .data(userResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    public UserResponse changeStatus(Long userId, Boolean status) {
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
    public boolean removeUser(Long userId) {
        User user = userRepository.findUserActive(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.getStatus()) user.setStatus(Boolean.FALSE);
        user.setRoles(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public CloudinaryResponse uploadAvatar(Long userId, MultipartFile file) throws IOException {
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(!user.getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return cloudinaryService.uploadImage(String.valueOf(userId), file);
    }

    private List<Sort.Order> handleSort(String sortBy){
        // Handle sort by Multiple Columns
        List<Sort.Order> sorts = new ArrayList<>();
            log.info("sortBy: {}", sortBy);
            if(StringUtils.hasLength(sortBy)) {
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                        sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        return sorts;
    }
}
