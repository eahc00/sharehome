package com.sharehome.notification.service.command;

public record NotificationCreateCommand(
        Long memberId,
        String content
) {
}
