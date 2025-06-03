package com.sharehome.member.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.UnauthorizedException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Member 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    @Test
    void 로그인_시_비밀번호가_일치하면_성공() {
        // given
        Member member = new Member(
                "email1234@domain.com",
                "하채리",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );

        // when&then
        Assertions.assertDoesNotThrow(() -> {
            member.login("Password1234@");
        });
    }

    @Test
    void 로그인_시_비밀번호가_다르면_예외() {
        // given
        Member member = new Member(
                "email1234@domain.com",
                "하채리",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );

        // when&then
        assertThatThrownBy(() ->
                member.login("Password1234@!")
        ).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 회원정보_수정() {
        // given
        Member member = new Member(
                "email1234@domain.com",
                "하채리",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );

        // when
        String nickname = "채리채리";
        Address address = new Address("대전", "대학로", "12345");
        member.update(nickname, address);

        // then
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getAddress()).isEqualTo(address);
    }
}