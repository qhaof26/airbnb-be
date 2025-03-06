package com.project.airbnb.controller;

import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.model.Notification;
import com.project.airbnb.service.Notification.NotificationService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("")
    public APIResponse<PageResponse<List<Notification>>> getNotifications(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<Notification>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get notifications")
                .data(notificationService.getNotifications(pageNo, pageSize))
                .build();
    }

    @DeleteMapping("/{notificationId}")
    public APIResponse<Void> deleteNotification(@PathVariable String notificationId){
        notificationService.deleteNotification(notificationId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete notification")
                .build();
    }
}
