package com.project.airbnb.repositories;

import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<User> {
}
