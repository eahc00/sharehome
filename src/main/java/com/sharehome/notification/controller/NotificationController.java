package com.sharehome.notification.controller;

import com.sharehome.notification.controller.request.NotificationCreateRequest;
import com.sharehome.notification.service.NotificationService;
import com.sharehome.notification.service.command.NotificationCreateCommand;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> createNotification(
            @RequestBody @Valid NotificationCreateRequest request
    ) {
        NotificationCreateCommand command = request.toCommand();
        Long notificationId = notificationService.createNotification(command);
        return ResponseEntity.created(URI.create("/notifications/" + notificationId)).build();
    }
}

