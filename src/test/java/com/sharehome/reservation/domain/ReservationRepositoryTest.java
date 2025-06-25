package com.sharehome.reservation.domain;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.숙소_Entity;
import static com.sharehome.fixture.PlaceFixture.숙소_Entity2;
import static org.assertj.core.api.Assertions.assertThat;

import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ReservationRepository 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    void 숙소와_예약기간을_받아_해당_기간_내_예약을_찾아온다() {
        // given
        Place place1 = 숙소_Entity();
        Place place2 = 숙소_Entity2();
        Member member = 회원_Entity();

        placeRepository.save(place1);
        placeRepository.save(place2);
        memberRepository.save(member);

        Reservation reservation = new Reservation(
                place1,
                member,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 8),
                2
        );

        Reservation reservation2 = new Reservation(
                place2,
                member,
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 4, 8),
                2
        );

        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);

        // when
        List<Reservation> reservations = reservationRepository.findAllByPlaceAndReservationDate(
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 5),
                place1
        );

        // then
        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.getFirst()).isEqualTo(reservation);
    }
}