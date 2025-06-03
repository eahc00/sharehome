package com.sharehome.member.service.command;

import java.time.LocalDate;

public record SignupCommand(
        String email,
        String name,
        LocalDate birth,
        String password
) {
}
