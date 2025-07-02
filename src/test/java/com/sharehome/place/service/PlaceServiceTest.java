package com.sharehome.place.service;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.불가능일_설정_command;
import static com.sharehome.fixture.PlaceFixture.숙소_Entity;
import static com.sharehome.fixture.PlaceFixture.숙소_등록_command;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
import jakarta.transaction.Transactional;
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
@Transactional
class PlaceServiceTest {

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
            Member savedMember = memberRepository.save(회원_Entity());
            memberId = savedMember.getId();
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

    @Nested
    class 숙소_예약불가일_설정_시 {

        Member savedMember;
        Place savedPlace;

        @BeforeEach
        void setup() {
            savedMember = memberRepository.save(회원_Entity());
            savedPlace = placeRepository.save(숙소_Entity(savedMember));
        }

        @Test
        void 설정_성공() {
            // given
            UnavailableDateUpdateCommand command = 불가능일_설정_command(
                    savedMember.getId(), savedPlace.getId()
            );

            // when&then
            Assertions.assertDoesNotThrow(() -> {
                placeService.updateUnavailableDate(command);
            });
        }
    }
}