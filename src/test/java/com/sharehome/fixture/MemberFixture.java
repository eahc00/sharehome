package com.sharehome.fixture;

import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.domain.Member;
import com.sharehome.member.service.command.SignupCommand;
import java.time.LocalDate;

public class MemberFixture {

    public static Member 회원_Entity() {
        return new Member(
                "email123@domain.com",
                "하영채",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );
    }

    public static SignupRequest 영채_회원가입_request() {
        return new SignupRequest(
                "email123@domain.com",
                "하영채",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );
    }

    public static SignupCommand 영채_회원가입_command() {
        return new SignupCommand(
                "email123@domain.com",
                "하영채",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );
    }

    public static LoginRequest 영채_로그인_request() {
        return new LoginRequest(
                "email123@domain.com",
                "Password1234@"
        );
    }
}
