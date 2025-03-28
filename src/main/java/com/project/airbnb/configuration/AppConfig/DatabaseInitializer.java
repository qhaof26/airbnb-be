package com.project.airbnb.configuration.AppConfig;

import com.project.airbnb.constant.PredefinedRole;
import com.project.airbnb.model.Role;
import com.project.airbnb.model.User;
import com.project.airbnb.repository.RoleRepository;
import com.project.airbnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String ADMIN_EMAIL;

    @Value("${app.admin.username}")
    private String ADMIN_USERNAME;

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD ;

    @Bean
    public ApplicationRunner initDatabase() {
        return args -> {

            if (!roleRepository.existsByRoleName(PredefinedRole.ADMIN_ROLE)) {
                log.info("In method init role ADMIN");
                Role adminRole = Role.builder()
                        .roleName(PredefinedRole.ADMIN_ROLE)
                        .description("This is the administrator and has all the permissions")
                        .build();
                roleRepository.save(adminRole);
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                if (!userRepository.existsByUsername(ADMIN_USERNAME) || !userRepository.existsByEmail(ADMIN_EMAIL)) {
                    User adminUser = User.builder()
                            .firstName("Quoc")
                            .lastName("Hao")
                            .username(ADMIN_USERNAME)
                            .password(passwordEncoder.encode(ADMIN_PASSWORD))
                            .email(ADMIN_EMAIL)
                            .roles(roles)
                            .status(Boolean.TRUE)
                            .build();
                    userRepository.save(adminUser);
                }
                log.info("Done method init role ADMIN");
            }

            Optional<Role> hostRole = roleRepository.findByRoleName(PredefinedRole.HOST_ROLE);
            if (hostRole.isEmpty()) {
                roleRepository.save(Role.builder()
                        .roleName(PredefinedRole.HOST_ROLE)
                        .description("This is host role")
                        .build());
            }

            Optional<Role> userRole = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE);
            if (userRole.isEmpty()) {
                roleRepository.save(Role.builder()
                        .roleName(PredefinedRole.GUEST_ROLE)
                        .description("This is guest role")
                        .permissions(null)
                        .build());
            }
        };
    }
}
