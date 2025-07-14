package com.sharehome.place.domain;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.채리호텔_Entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.ConflictException;
import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.member.domain.Member;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("Place 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PlaceTest {

    @Test
    void 숙소등록_사용자를_인증한다() {
        // given
        Member member = 회원_Entity();
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

        // when&then
        Assertions.assertDoesNotThrow(() -> {
            place.validateMember(member);
        });
    }

    @Test
    void 인증되지_않은_사용자면_예외() {
        // given
        Member member = 회원_Entity();
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

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
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

        LocalDate unavailableDate1 = LocalDate.of(2025, 8, 1);
        LocalDate unavailableDate2 = LocalDate.of(2025, 8, 15);

        List<LocalDate> unavailableDates = new ArrayList<>();
        unavailableDates.add(unavailableDate1);
        unavailableDates.add(unavailableDate2);

        // when
        place.addUnavailableDate(member, unavailableDates);

        // then
        assertThat(place.getUnavailableDateValues().size()).isEqualTo(2);
        assertThat(place.getUnavailableDateValues()).contains(unavailableDate1, unavailableDate2);
    }

    @Test
    void 추가하는_숙소_예약_불가일이_이미_있으면_추가되지_않는다() {
        // given
        Member member = 회원_Entity();
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

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
        assertThat(place.getUnavailableDateValues().size()).isEqualTo(2);
    }

    @Test
    void 숙소_예약_불가일이_예약기간에_존재하지_않아야_한다() {
        // given
        Member member = 회원_Entity();
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

        LocalDate unavailableDate1 = LocalDate.of(2025, 8, 1);
        LocalDate unavailableDate2 = LocalDate.of(2025, 8, 15);

        List<LocalDate> unavailableDates = new ArrayList<>();
        unavailableDates.add(unavailableDate1);
        unavailableDates.add(unavailableDate2);

        place.addUnavailableDate(member, unavailableDates);

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            place.validateAvailableDate(
                    LocalDate.of(2025, 8, 13),
                    LocalDate.of(2025, 8, 14)
            );
        });
    }

    @Test
    void 숙소_예약_불가일이_예약기간에_존재하면_예외() {
        // given
        Member member = 회원_Entity();
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

        LocalDate unavailableDate1 = LocalDate.of(2025, 8, 1);
        LocalDate unavailableDate2 = LocalDate.of(2025, 8, 15);

        List<LocalDate> unavailableDates = new ArrayList<>();
        unavailableDates.add(unavailableDate1);
        unavailableDates.add(unavailableDate2);

        place.addUnavailableDate(member, unavailableDates);

        // when & then
        assertThatThrownBy(() ->
                place.validateAvailableDate(
                        LocalDate.of(2025, 8, 14),
                        LocalDate.of(2025, 8, 16)
                )
        ).isInstanceOf(ConflictException.class)
                .hasMessageContaining("예약이 불가능한 날짜입니다.");
    }

    @Test
    void 숙소_정보를_변경한다() {
        // given
        Member member = 회원_Entity();
        ReflectionTestUtils.setField(member, "id", 100L);
        Place place = 채리호텔_Entity(member);

        // when
        place.changePlaceInfo(
                member,
                "대전호텔",
                2,
                place.getBedroomCount(),
                place.getDetailInfo(),
                60_000L,
                75_000L,
                LocalTime.of(16, 0),
                LocalTime.of(11, 0),
                place.getAmenities()
        );

        // then
        assertThat(place.getName()).isEqualTo("대전호텔");
        assertThat(place.getBedCount()).isEqualTo(2);
        assertThat(place.getWeekdayPrice()).isEqualTo(60_000L);
        assertThat(place.getWeekendPrice()).isEqualTo(75_000L);
        assertThat(place.getCheckInTime()).isEqualTo(LocalTime.of(16, 0, 0));
        assertThat(place.getCheckOutTime()).isEqualTo(LocalTime.of(11, 0, 0));
    }
}