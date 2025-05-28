package com.sharehome.member.controller.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.sharehome.common.exception.BadRequestException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class SignupRequest {

    @Pattern(
            regexp = "^[a-zA-Z0-9.]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
            message = "이메일 형식이 올바르지 않습니다."
    )
    @NotNull(message = "이메일은 필수로 입력해야 합니다.")
    private String email;

    @Pattern(
            regexp = "^(?:[가-힣]{2,20}|[a-zA-Z]{2,20})$",
            message = "이름 형식이 올바르지 않습니다."
    )
    @NotNull(message = "사용자 이름은 필수로 입력해야 합니다.")
    private String name;

    @Past
    @NotNull(message = "생년월일은 필수로 입력해야 합니다.")
    @JsonFormat(shape = STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
    private LocalDate birth;

    @Pattern(
            regexp = "^(?!.*\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#,.<>?])[A-Za-z\\d!@#,.<>?]{8,50}$",
            message = "비밀번호 형식이 올바르지 않습니다."
    )
    @NotNull(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;

    public SignupRequest(String email, String name, LocalDate birth, String password) {
        this.email = email;
        this.name = name;
        validateBirth(birth);
        this.birth = birth;
        this.password = password;
    }

    private void validateBirth(LocalDate birth) {
        int americanAge = LocalDate.now().minusYears(birth.getYear()).getYear();

        if (birth.plusYears(americanAge).isAfter(LocalDate.now())) {
            americanAge = americanAge -1;
        }

        if (americanAge < 18) {
            throw new BadRequestException("만 18세 이상의 성인만 회원가입할 수 있습니다.");
        }
    }
}
