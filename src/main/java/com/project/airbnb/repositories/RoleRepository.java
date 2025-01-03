package com.project.airbnb.repositories;

import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<User> {
    Optional<Role> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}
