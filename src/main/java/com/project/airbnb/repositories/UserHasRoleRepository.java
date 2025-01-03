package com.project.airbnb.repositories;

import com.project.airbnb.models.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface UserHasRoleRepository extends JpaRepository<UserHasRole, String> {
    @Query(value = "SELECT uhr.role.roleName from UserHasRole uhr where uhr.user.id = :userId")
    Set<String> findRolesByUserId(String userId);

    List<UserHasRole> findAllByUserId(String userId);
    List<UserHasRole> findAllByRoleId(String roleId);
}
