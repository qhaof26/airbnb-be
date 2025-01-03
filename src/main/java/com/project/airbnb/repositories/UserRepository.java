package com.project.airbnb.repositories;

import com.project.airbnb.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    @Query(value = "select u from User u where u.id = :userId and u.isActive = true ")
    Optional<User> findUserActive(String userId);

    @Query(value = "select u from User u where u.isActive = true ")
    Page<User> findAllUserActive(Pageable pageable);

    @Query(value = "select u from User u where u.isActive = false ")
    Page<User> findAllUserBlock(Pageable pageable);

    boolean existsByUsername(String username);
}
