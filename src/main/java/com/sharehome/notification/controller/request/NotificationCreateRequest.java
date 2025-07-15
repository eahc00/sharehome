package com.sharehome.notification.controller.request;

import com.sharehome.notification.service.command.NotificationCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateRequest(

        @NotNull(message = "회원 id는 null이어서는 안됩니다.")
        Long memberId,

        @NotNull(message = "알림 내용은 null이어서는 안됩니다.")
        @NotBlank(message = "알림 내용은 공백이어서는 안됩니다.")
        String content
) {

    public NotificationCreateCommand toCommand() {
        return new NotificationCreateCommand(
                memberId,
                content
        );
    }
}
