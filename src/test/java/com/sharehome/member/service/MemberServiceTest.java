package com.sharehome.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.domain.Address;
import com.sharehome.common.exception.ConflictException;
import com.sharehome.common.exception.NotFoundException;
import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.member.service.command.ChangePasswordCommand;
import com.sharehome.member.service.command.SignupCommand;
import com.sharehome.member.service.command.UpdateMemberCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
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
@SuppressWarnings("NonAsciiCharacters")
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
            memberService.join(new SignupCommand(
                    "chxxry00@naver.com",
                    "하채리",
                    LocalDate.of(2002, 10, 9),
                    "Abc123@!"
            ));

            // when & then
            assertThatThrownBy(() ->
                    memberService.join(new SignupCommand(
                            "chxxry00@naver.com",
                            "하채리",
                            LocalDate.of(2002, 10, 9),
                            "Abc123@!"
                    ))
            ).isInstanceOf(ConflictException.class)
                    .hasMessage("해당 이메일로 이미 가입한 회원이 있습니다");
        }

        @Test
        void 중복되는_이메일이_없으면_회원가입에_성공한다() {
            // when
            Long memberId = memberService.join(new SignupCommand(
                    "chxxry00@naver.com",
                    "하채리",
                    LocalDate.of(2002, 10, 9),
                    "Abc123@!"
            ));

            // then
            assertThat(memberId).isNotNull();
        }
    }

    @Nested
    class 로그인_시 {

        private final SignupCommand command = new SignupCommand(
                "email123@domain.com",
                "하영채",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );

        @BeforeEach
        void setUp() {
            memberService.join(command);
        }

        @Test
        void 로그인_성공() {
            // when&then
            Assertions.assertDoesNotThrow(() -> {
                memberService.login(command.email(), command.password());
            });
        }

        @Test
        void 이메일이_없으면_예외() {
            // when&then
            assertThatThrownBy(() ->
                    memberService.login("invalid@domain.com", command.password())
            ).isInstanceOf(UnauthorizedException.class);
        }

        @Test
        void 비밀번호가_틀리면_예외() {
            // when&then
            assertThatThrownBy(() ->
                    memberService.login(command.email(), "Invalid1234@")
            ).isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    class 계정관리_시 {

        private Long memberId;

        @BeforeEach
        void setUp() {
            SignupCommand signupCommand = new SignupCommand(
                    "email123@domain.com",
                    "하영채",
                    LocalDate.of(2002, 10, 9),
                    "Password1234@"
            );
            memberId = memberService.join(signupCommand);
        }

        @Test
        void 해당_id를_가진_회원의_정보를_수정할_수_있다() {
            // given
            UpdateMemberCommand command = new UpdateMemberCommand(
                    memberId,
                    "채리채리",
                    new Address("대전", "대학로", "12345")
            );

            // when
            memberService.updateMember(command);

            // then
            Member member = memberRepository.findById(memberId).get();
            assertThat(member.getNickname()).isEqualTo(command.nickname());
            assertThat(member.getAddress()).isEqualTo(command.address());
        }

        @Test
        void 해당_id를_가진_회원이_없으면_예외() {
            // given
            UpdateMemberCommand command = new UpdateMemberCommand(
                    100L,
                    "채리채리",
                    new Address("대전", "대학로", "12345")
            );

            // when&then
            assertThatThrownBy(() ->
                    memberService.updateMember(command)
            ).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class 비밀번호_변경_시 {

        private Long memberId;
        private String oldPassword;

        @BeforeEach
        void setUp() {
            SignupCommand signupCommand = new SignupCommand(
                    "email123@domain.com",
                    "하영채",
                    LocalDate.of(2002, 10, 9),
                    "Password1234@"
            );
            memberId = memberService.join(signupCommand);
            oldPassword = signupCommand.password();
        }

        @Test
        void 해당_id를_가진_회원의_비밀번호를_변경할_수_있다() {
            // given
            String newPassword = "newPassword1234@";
            ChangePasswordCommand command = new ChangePasswordCommand(
                    memberId,
                    oldPassword,
                    newPassword
            );

            // when
            memberService.changePassword(command);

            // then
            Member member = memberRepository.findById(memberId).get();
            assertThat(member.getPassword()).isEqualTo(newPassword);
        }

        @Test
        void 해당_id를_가진_회원이_없으면_예외() {
            // given
            ChangePasswordCommand command = new ChangePasswordCommand(
                    100L,
                    oldPassword,
                    "newPassword1234@"
            );

            // when&then
            assertThatThrownBy(() ->
                    memberService.changePassword(command)
            ).isInstanceOf(NotFoundException.class);
        }
    }
}