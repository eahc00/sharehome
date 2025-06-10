package com.sharehome.fixture;

import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.service.command.SignupCommand;
import java.time.LocalDate;

public class MemberFixture {

    public static SignupRequest 영채_회원가입_request =
            new SignupRequest(
                    "email123@domain.com",
                    "하영채",
                    LocalDate.of(2002, 10, 9),
                    "Password1234@"
            );

    public static SignupCommand 영채_회원가입_command =
            new SignupCommand(
                    "email123@domain.com",
                    "하영채",
                    LocalDate.of(2002, 10, 9),
                    "Password1234@"
            );

    public static LoginRequest 영채_로그인_request = new LoginRequest(
            "email123@domain.com",
            "Password1234@"
    );
}
