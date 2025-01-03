package com.project.airbnb.configurations.Initializer;

import com.project.airbnb.constants.PredefinedRole;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.models.UserHasRole;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserHasRoleRepository;
import com.project.airbnb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RoleUserInitializer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserHasRoleRepository userHasRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";
    @NonFinal
    static final String ADMIN_USERNAME = "admin";
    @NonFinal
    static final String ADMIN_PASSWORD = "123456";

    @Bean
    public ApplicationRunner initDatabase(){
        return args -> {

            if(!roleRepository.existsByRoleName(PredefinedRole.ADMIN_ROLE)){
                Role adminRole = Role.builder()
                        .roleName(PredefinedRole.ADMIN_ROLE)
                        .description("This is the administrator and has all the permissions")
                        .build();
                roleRepository.save(adminRole);

                if(userRepository.existsByEmail(ADMIN_EMAIL)) throw new AppException(ErrorCode.EMAIL_EXISTED);
                if(userRepository.existsByUsername(ADMIN_USERNAME)) throw new AppException(ErrorCode.USERNAME_EXISTED);
                User adminUser = User.builder()
                        .firstName("Quoc")
                        .lastName("Hao")
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .email(ADMIN_EMAIL)
                        .isActive(true)
                        .build();
                userRepository.save(adminUser);

                UserHasRole userHasRole = UserHasRole.builder()
                        .role(adminRole)
                        .user(adminUser)
                        .build();
                userHasRoleRepository.save(userHasRole);
            }

            Optional<Role> hostRole = roleRepository.findByRoleName(PredefinedRole.HOST_ROLE);
            if(hostRole.isEmpty()){
                roleRepository.save(Role.builder()
                                .roleName(PredefinedRole.HOST_ROLE)
                                .description("This is host role")
                        .build());
            }

            Optional<Role> userRole = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE);
            if(userRole.isEmpty()){
                roleRepository.save(Role.builder()
                        .roleName(PredefinedRole.GUEST_ROLE)
                        .description("This is guest role")
                        .permissions(null)
                        .build());
            }
        };
    }
}
