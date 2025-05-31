package com.sharehome.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    void 이메일로_회원을_찾아온다() {
        // given
        Member member = new Member(
                "chxxry@naver.com",
                "하채리",
                LocalDate.of(2002, 10, 9),
                "Password123@"
        );
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findByEmail(member.getEmail()).get();

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void 회원이_없는_경우_null_반환() {
        // when
        Optional<Member> findMember = memberRepository.findByEmail("chxxry@naver.com");

        // then
        assertThat(findMember.isEmpty()).isTrue();
    }
}