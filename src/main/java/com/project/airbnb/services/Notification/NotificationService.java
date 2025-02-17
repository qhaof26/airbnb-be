package com.project.airbnb.services.Notification;

import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Notification;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.NotificationRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotificationService{
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @PreAuthorize("isAuthenticated()")
    public PageResponse<List<Notification>> getNotifications(int pageNo, int pageSize) {
        User user = getUserLogin();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Notification> notificationPage = notificationRepository.findByUserOrderByCreatedAt(user.getId(), pageable);
        List<Notification> notifications = notificationPage.getContent();
        for(Notification notification : notifications){
            simpMessagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", notification);
        }

        return PageResponse.<List<Notification>>builder()
                .page(pageNo + 1)
                .size(pageSize)
                .totalPage(notificationPage.getTotalPages())
                .totalElement(notificationPage.getTotalElements())
                .data(notifications)
                .build();
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void sendNotification(User receiver, String title, String body) {
        Notification notification = Notification.builder()
                .user(receiver)
                .title(title)
                .body(body)
                .isRead(0)
                .build();
        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/notifications", notification);
        log.info("Sending notification to user: {}", receiver.getEmail());
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        if (!getUserLogin().equals(notification.getUser())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        notification.setIsRead(1);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        if (!getUserLogin().equals(notification.getUser())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        notificationRepository.delete(notification);
    }

    private User getUserLogin(){
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
