package com.sharehome.member.service.command;

import com.sharehome.member.domain.Member;
import java.time.LocalDate;

public record SignupCommand(
        String email,
        String name,
        LocalDate birth,
        String password
) {

    public Member toMember() {
        return new Member(
                email,
                name,
                birth,
                password
        );
    }
}
