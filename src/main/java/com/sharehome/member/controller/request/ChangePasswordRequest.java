package com.sharehome.member.controller.request;

import com.sharehome.member.service.command.ChangePasswordCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @NotNull(message = "현재 비밀번호는 null이어서는 안됩니다.")
        @NotBlank(message = "현재 비밀번호는 공백이어서는 안됩니다.")
        String oldPassword,

        @Pattern(
                regexp = "^(?!.*\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#,.<>?])[A-Za-z\\d!@#,.<>?]{8,50}$",
                message = "비밀번호 형식이 올바르지 않습니다."
        )
        @NotNull(message = "새 비밀번호는 null이어서는 안됩니다.")
        String newPassword
) {

    public ChangePasswordCommand toCommand(Long memberId) {
        return new ChangePasswordCommand(memberId, oldPassword, newPassword);
    }
}
