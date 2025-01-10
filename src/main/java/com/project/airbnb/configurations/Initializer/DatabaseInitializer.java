package com.project.airbnb.configurations.Initializer;

import com.project.airbnb.constants.PredefinedRole;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.services.Location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
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
    private final LocationService provinceService;

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
                log.info("In method init role ADMIN");
                Role adminRole = Role.builder()
                        .roleName(PredefinedRole.ADMIN_ROLE)
                        .description("This is the administrator and has all the permissions")
                        .build();
                roleRepository.save(adminRole);
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                if(userRepository.existsByEmail(ADMIN_EMAIL)) throw new AppException(ErrorCode.EMAIL_EXISTED);
                if(userRepository.existsByUsername(ADMIN_USERNAME)) throw new AppException(ErrorCode.USERNAME_EXISTED);
                User adminUser = User.builder()
                        .firstName("Quoc")
                        .lastName("Hao")
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .email(ADMIN_EMAIL)
                        .roles(roles)
                        .build();
                userRepository.save(adminUser);

                log.info("Done method init role ADMIN");
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

            if(provinceService.getProvinces().isEmpty()){
                provinceService.fetchAndSaveProvinces();
                provinceService.fetchAndSaveDistricts();
                provinceService.fetchAndSaveWards();
                log.warn("Inside method init");
            }
        };
    }
}
