package com.project.airbnb.repositories;

import com.project.airbnb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
