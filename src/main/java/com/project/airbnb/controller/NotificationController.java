package com.project.airbnb.controller;

import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.model.Notification;
import com.project.airbnb.service.Notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/notifications")
@Tag(name = "Notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get list of notification", description = "Send a request via this API to get list of notification")
    @GetMapping
    public APIResponse<PageResponse<List<Notification>>> getNotifications(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<Notification>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get notifications successfully")
                .data(notificationService.getNotifications(pageNo, pageSize))
                .build();
    }

    @Operation(summary = "Delete notification permanently", description = "Send a request via this API to delete notification permanently")
    @DeleteMapping("/{notificationId}")
    public APIResponse<Void> deleteNotification(@PathVariable String notificationId){
        notificationService.deleteNotification(notificationId);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete notification successfully")
                .build();
    }
}
