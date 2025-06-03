package com.sharehome.member.service.command;

public record ChangePasswordCommand(
        Long memberId,
        String oldPassword,
        String newPassword
) {
}
