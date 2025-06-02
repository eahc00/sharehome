package com.sharehome.member.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(

        @NotNull(message = "이메일은 null이어서는 안됩니다.")
        @NotBlank(message = "이메일은 공백이어서는 안됩니다.")
        String email,

        @NotBlank(message = "비밀번호는 null이어서는 안됩니다.")
        @NotNull(message = "비밀번호는 공백이어서는 안됩니다.")
        String password
) {
}
