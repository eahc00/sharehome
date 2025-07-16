package com.sharehome.place.service;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.불가능일_삭제_command;
import static com.sharehome.fixture.PlaceFixture.불가능일_설정_command;
import static com.sharehome.fixture.PlaceFixture.숙소_등록_command;
import static com.sharehome.fixture.PlaceFixture.숙소_수정_command;
import static com.sharehome.fixture.PlaceFixture.채리호텔_Entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import com.sharehome.place.service.command.PlaceUpdateCommand;
import com.sharehome.place.service.command.UnavailableDateDeleteCommand;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
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
import org.springframework.transaction.annotation.Transactional;

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
    class 숙소_예약불가일 {

        Member savedMember;
        Place savedPlace;

        @BeforeEach
        void setup() {
            savedMember = memberRepository.save(회원_Entity());
            savedPlace = placeRepository.save(채리호텔_Entity(savedMember));
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

        @Test
        void 제거_성공() {
            // given
            UnavailableDateDeleteCommand command = 불가능일_삭제_command(
                    savedMember.getId(), savedPlace.getId()
            );

            savedPlace.addUnavailableDate(List.of(
                    LocalDate.of(2025, 8, 1),
                    LocalDate.of(2025, 8, 15)
            ));

            // when&then
            Assertions.assertDoesNotThrow(() -> {
                placeService.deleteUnavailableDate(command);
            });
        }
    }

    @Test
    void 숙소_조회_시_숙소_id에_해당하는_숙소를_찾아_반환한다() {
        // given
        Member savedMember = memberRepository.save(회원_Entity());
        Place savedPlace = placeRepository.save(채리호텔_Entity(savedMember));

        // when
        Place place = placeService.getPlace(savedPlace.getId());
        // then
        assertThat(place.getId()).isEqualTo(savedPlace.getId());

    }

    @Test
    void 숙소_정보를_수정한다() {
        // given
        Member savedMember = memberRepository.save(회원_Entity());
        Place savedPlace = placeRepository.save(채리호텔_Entity(savedMember));
        PlaceUpdateCommand command = 숙소_수정_command(
                savedMember.getId(), savedPlace.getId()
        );

        // when
        placeService.updatePlace(command);

        // then
        assertThat(savedPlace.getName()).isEqualTo(command.name());
        assertThat(savedPlace.getBedCount()).isEqualTo(command.bedCount());
        assertThat(savedPlace.getBedroomCount()).isEqualTo(command.bedroomCount());
        assertThat(savedPlace.getDetailInfo()).isEqualTo(command.detailInfo());
        assertThat(savedPlace.getWeekdayPrice()).isEqualTo(command.weekdayPrice());
        assertThat(savedPlace.getWeekendPrice()).isEqualTo(command.weekendPrice());
        assertThat(savedPlace.getCheckInTime()).isEqualTo(command.checkInTime());
        assertThat(savedPlace.getCheckOutTime()).isEqualTo(command.checkOutTime());
        assertThat(savedPlace.getAmenities()).isEqualTo(command.amenities());
    }
}
