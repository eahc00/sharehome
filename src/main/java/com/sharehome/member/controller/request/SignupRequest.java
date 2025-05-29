package com.sharehome.member.controller.request;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sharehome.common.exception.BadRequestException;
import com.sharehome.member.service.command.MemberCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record SignupRequest(
        @Pattern(
                regexp = "^[a-zA-Z0-9.]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
                message = "이메일 형식이 올바르지 않습니다."
        )
        @NotNull(message = "이메일은 필수로 입력해야 합니다.")
        String email
        ,
        @Pattern(
            regexp = "^(?:[가-힣]{2,20}|[a-zA-Z]{2,20})$",
            message = "이름 형식이 올바르지 않습니다."
        )
        @NotNull(message = "사용자 이름은 필수로 입력해야 합니다.")
        String name,

        @Past
        @NotNull(message = "생년월일은 필수로 입력해야 합니다.")
        @JsonFormat(shape = STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        LocalDate birth,

        @Pattern(
            regexp = "^(?!.*\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#,.<>?])[A-Za-z\\d!@#,.<>?]{8,50}$",
            message = "비밀번호 형식이 올바르지 않습니다."
        )
        @NotNull(message = "비밀번호는 필수로 입력해야 합니다.")
        String password
) {

    public SignupRequest {
        validateBirth(birth);
    }

    public MemberCommand toCommand() {
        return new MemberCommand(
                email, name, birth, password
        );
    }

    private static void validateBirth(LocalDate birth) {
        int americanAge = LocalDate.now().minusYears(birth.getYear()).getYear();

        if (birth.plusYears(americanAge).isAfter(LocalDate.now())) {
            americanAge = americanAge -1;
        }

        if (americanAge < 18) {
            throw new BadRequestException("만 18세 이상의 성인만 회원가입할 수 있습니다.");
        }
    }
}
