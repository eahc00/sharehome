package com.sharehome.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("비밀번호 (Password) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
class PasswordTest {

    @Test
    void 비밀번호를_해싱한다() {
        // given
        String value = "Abc123!@#";

        // when
        Password password = Password.hashPassword(value);

        // then
        assertThat(password.getSalt()).isNotNull();
        assertThat(password.getHashedPassword()).isNotEqualTo(value);
    }

    @Test
    void 비밀번호_일치여부_검증() {
        // given
        String value = "Abc123!@#";
        Password password = Password.hashPassword(value);

        // when
        boolean isEqual = password.checkPassword(value);

        // then
        assertThat(isEqual).isTrue();
    }
}