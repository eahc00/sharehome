package com.sharehome.place.service;

import static com.sharehome.fixture.MemberFixture.영채_회원가입_command;
import static com.sharehome.fixture.PlaceFixture.숙소_등록_command;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.member.service.MemberService;
import com.sharehome.member.service.command.SignupCommand;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.service.command.PlaceRegisterCommand;
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
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PlaceService 은(는)")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PlaceServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PlaceService placeService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PlaceRepository placeRepository;

    @BeforeEach
    void setup() {
        placeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    class 숙소_등록_시 {

        Long memberId;

        @BeforeEach
        void setup() {
            SignupCommand signupCommand = 영채_회원가입_command();
            memberId = memberService.join(signupCommand);
        }


        @Test
        void 등록_성공() {
            // given
            PlaceRegisterCommand placeRegisterCommand = 숙소_등록_command(memberId);

            // when&then
            Assertions.assertDoesNotThrow(() -> {
                placeService.register(placeRegisterCommand);
            });
        }

        @Test
        void 해당_아이디를_가진_회원이_없으면_예외() {
            // given
            PlaceRegisterCommand placeRegisterCommand = 숙소_등록_command(100L);

            // when&then
            assertThatThrownBy(() ->
                    placeService.register(placeRegisterCommand)
            ).isInstanceOf(NotFoundException.class);
        }
    }
}