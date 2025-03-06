package com.project.airbnb.repository;

import com.project.airbnb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query(value = "select u from User u join fetch u.roles where u.id = :userId and u.status = TRUE ")
    Optional<User> findUserActive(Long userId);

    // @EntityGraph(attributePaths = {"roles"})
    @Query(value = "select u from User u join fetch u.roles where u.status = TRUE ")
    Page<User> findAllUserActive(Pageable pageable);

    @Query(value = "select u from User u join fetch u.roles where u.status = false ")
    Page<User> findAllUserBlock(Pageable pageable);

    boolean existsByUsername(String username);
}
