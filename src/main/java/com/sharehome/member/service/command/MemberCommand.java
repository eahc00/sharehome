package com.sharehome.member.service.command;

import java.time.LocalDate;

public record MemberCommand(
        String email,
        String name,
        LocalDate birth,
        String password
) {
}
