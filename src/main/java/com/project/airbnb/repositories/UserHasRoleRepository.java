package com.project.airbnb.repositories;

import com.project.airbnb.models.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRoleRepository extends JpaRepository<UserHasRole, String> {
}
