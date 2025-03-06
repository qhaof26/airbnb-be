package com.project.airbnb.service.Notification;

import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.model.Notification;
import com.project.airbnb.model.User;

import java.util.List;

public interface INotificationService {
    PageResponse<List<Notification>> getNotifications(int pageNo, int pageSize);
    void sendNotification(User receiver, String title, String body);
    void markAsRead(String notificationId);
    void deleteNotification(String notificationId);
}
