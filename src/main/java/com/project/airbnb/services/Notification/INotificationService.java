package com.project.airbnb.services.Notification;

import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.models.Notification;
import com.project.airbnb.models.User;

import java.util.List;

public interface INotificationService {
    PageResponse<List<Notification>> getNotifications(int pageNo, int pageSize);
    void sendNotification(User receiver, String title, String body);
    void markAsRead(String notificationId);
    void deleteNotification(String notificationId);
}
