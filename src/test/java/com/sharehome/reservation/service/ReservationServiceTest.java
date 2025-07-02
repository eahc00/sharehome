package com.sharehome.reservation.service;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.숙소_Entity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.BadRequestException;
import com.sharehome.common.exception.ConflictException;
import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.reservation.domain.Reservation;
import com.sharehome.reservation.domain.ReservationRepository;
import com.sharehome.reservation.service.command.ReservePlaceCommand;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
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
@DisplayName("ReservationService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PlaceRepository placeRepository;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
        placeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Nested
    class 예약_생성_시 {

        private Member savedMember;
        private Place savedPlace;

        @BeforeEach
        void setup() {
            savedMember = memberRepository.save(회원_Entity());
            savedPlace = placeRepository.save(숙소_Entity(savedMember));
            Reservation reservation = new Reservation(
                    savedPlace,
                    savedMember,
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 3, 5),
                    4
            );
            reservationRepository.save(reservation);
        }

        @Test
        void 예약_성공() {
            // given
            ReservePlaceCommand command = new ReservePlaceCommand(
                    savedMember.getId(),
                    savedPlace.getId(),
                    LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 5),
                    4);

            // when&then
            Assertions.assertDoesNotThrow(() ->
                    reservationService.reservePlace(command)
            );
        }

        @Test
        void 일치하는_회원이_없으면_예외() {
            // given
            ReservePlaceCommand command = new ReservePlaceCommand(
                    100L,
                    savedPlace.getId(),
                    LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 5),
                    4);

            // when&then
            assertThatThrownBy(() ->
                    reservationService.reservePlace(command)
            ).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 일치하는_숙소가_없으면_예외() {
            // given
            ReservePlaceCommand command = new ReservePlaceCommand(
                    savedMember.getId(),
                    100L,
                    LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 5),
                    4);

            // when&then
            assertThatThrownBy(() ->
                    reservationService.reservePlace(command)
            ).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 해당_숙소에_날짜가_중복되는_예약이_있으면_예외() {
            // given
            ReservePlaceCommand command = new ReservePlaceCommand(
                    savedMember.getId(),
                    savedPlace.getId(),
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 3, 3),
                    4
            );

            // when&then
            assertThatThrownBy(() ->
                    reservationService.reservePlace(command)
            ).isInstanceOf(ConflictException.class);
        }

        @Test
        void 최대_게스트_인원을_초과하면_예외() {
            // given
            ReservePlaceCommand command = new ReservePlaceCommand(
                    savedMember.getId(),
                    savedPlace.getId(),
                    LocalDate.of(2025, 4, 1),
                    LocalDate.of(2025, 4, 5),
                    5
            );

            // when&then
            assertThatThrownBy(() ->
                    reservationService.reservePlace(command)
            ).isInstanceOf(BadRequestException.class);
        }

        @Test
        void 예약_불가능한_날짜면_예외() {
            // given
            savedPlace.addUnavailableDate(
                    savedMember,
                    List.of(LocalDate.of(2025, 8, 15))
            );

            ReservePlaceCommand command = new ReservePlaceCommand(
                    savedMember.getId(),
                    savedPlace.getId(),
                    LocalDate.of(2025, 8, 14),
                    LocalDate.of(2025, 8, 16),
                    4
            );

            // when
            assertThatThrownBy(() ->
                    reservationService.reservePlace(command)
            ).isInstanceOf(ConflictException.class);
        }
    }
}