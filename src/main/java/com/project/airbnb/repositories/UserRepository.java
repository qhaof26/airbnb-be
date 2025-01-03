package com.project.airbnb.repositories;

import com.project.airbnb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    @Query(value = "select u from User u where u.id = :userId and u.isActive = true ")
    Optional<User> findUserActive(String userId);

    boolean existsByUsername(String username);
}
