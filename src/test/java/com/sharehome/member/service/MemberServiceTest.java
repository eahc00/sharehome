package com.sharehome.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.ConflictException;
import com.sharehome.member.domain.MemberRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("MemberService 은(는)")
@SuppressWarnings("NonAsciiChracters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Nested
    class 회원가입_시 {

        @Test
        void 이메일이_중복되면_예외() {
            // given
            memberService.join("a", "a", LocalDate.of(2002, 10, 9), "a");

            // when & then
            assertThatThrownBy(() ->
                    memberService.join("a", "a", LocalDate.of(2002, 10, 9), "a")
            ).isInstanceOf(ConflictException.class)
                    .hasMessage("해당 이메일로 이미 가입한 회원이 있습니다");
        }

        @Test
        void 중복되는_이메일이_없으면_회원가입에_성공한다() {
            // when
            Long memberId = memberService.join("a", "a", LocalDate.of(2002, 10, 9), "a");

            // then
            assertThat(memberId).isNotNull();

        }
    }
}