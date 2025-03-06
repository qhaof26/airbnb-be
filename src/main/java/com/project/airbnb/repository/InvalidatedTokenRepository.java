package com.project.airbnb.repository;

import com.project.airbnb.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    @Query(value = "select i.id from InvalidatedToken i where i.expiryTime < :currentDateTime")
    List<String> findExpiredToken(@Param("currentDateTime")LocalDateTime currentDateTime);
}
