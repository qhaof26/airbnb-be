package com.project.airbnb.configuration.Initializer;

import com.project.airbnb.enums.RoleType;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.models.UserHasRole;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserHasRoleRepository;
import com.project.airbnb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoleUserInitializer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserHasRoleRepository userHasRoleRepository;

    @Bean
    public ApplicationRunner initDatabase(){
        return args -> {
            if(roleRepository.count() == 0){
                Role role = Role.builder()
                        .roleName("ADMIN")
                        .description("This is the administrator and has all the permissions")
                        .permissions(null)
                        .build();
                User adminUser = User.builder()
                        .username("admin")
                        .password("123456")
                        .email("admin@gmail.com")
                        .build();
                UserHasRole userHasRole = UserHasRole.builder()
                        .user(adminUser)
                        .role(role)
                        .build();
                roleRepository.save(role);
                userRepository.save(adminUser);
                //userHasRoleRepository.save(userHasRole);

            }
        };
    }
}
