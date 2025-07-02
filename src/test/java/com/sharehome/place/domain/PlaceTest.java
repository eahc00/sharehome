package com.sharehome.place.domain;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.숙소_Entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.member.domain.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Place 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PlaceTest {

    @Test
    void 숙소등록_사용자를_인증한다() {
        // given
        Member member = 회원_Entity();
        Place place = 숙소_Entity(member);

        // when&then
        Assertions.assertDoesNotThrow(() -> {
            place.validateMember(member);
        });
    }

    @Test
    void 인증되지_않은_사용자면_예외() {
        // given
        Member member = 회원_Entity();
        Place place = 숙소_Entity(member);

        Member member2 = 회원_Entity();

        // when&then
        assertThatThrownBy(() ->
                place.validateMember(member2)
        ).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 숙소_예약_불가일을_추가한다() {
        // given
        Member member = 회원_Entity();
        Place place = 숙소_Entity(member);

        LocalDate unavailableDate1 = LocalDate.of(2025, 8, 1);
        LocalDate unavailableDate2 = LocalDate.of(2025, 8, 15);

        List<LocalDate> unavailableDates = new ArrayList<>();
        unavailableDates.add(unavailableDate1);
        unavailableDates.add(unavailableDate2);

        // when
        place.addUnavailableDate(member, unavailableDates);

        // then
        assertThat(place.getUnavailableDate().size()).isEqualTo(2);
        assertThat(place.getUnavailableDate()).contains(unavailableDate1, unavailableDate2);
    }

    @Test
    void 추가하는_숙소_예약일이_이미_있으면_추가되지_않는다() {
        // given
        Member member = 회원_Entity();
        Place place = 숙소_Entity(member);

        LocalDate unavailableDate1 = LocalDate.of(2025, 8, 1);
        LocalDate unavailableDate2 = LocalDate.of(2025, 8, 15);
        LocalDate unavailableDate3 = LocalDate.of(2025, 8, 1);

        List<LocalDate> unavailableDates = new ArrayList<>();
        unavailableDates.add(unavailableDate1);
        unavailableDates.add(unavailableDate2);

        place.addUnavailableDate(member, unavailableDates);

        // when
        place.addUnavailableDate(member, List.of(unavailableDate3));

        // then
        assertThat(place.getUnavailableDate().size()).isEqualTo(2);
    }
}