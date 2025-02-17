package com.project.airbnb.repositories;

import com.project.airbnb.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    @Query(value = "select n from Notification n where n.user.id = :userId order by n.createdAt desc")
    Page<Notification> findByUserOrderByCreatedAt(Long userId, Pageable pageable);
}
