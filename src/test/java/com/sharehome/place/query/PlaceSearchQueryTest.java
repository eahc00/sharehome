package com.sharehome.place.query;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.서울게하_Entity;
import static com.sharehome.fixture.PlaceFixture.인천펜션_Entity;
import static com.sharehome.fixture.PlaceFixture.채리모텔_Entity;
import static com.sharehome.fixture.PlaceFixture.채리호텔_Entity;
import static org.assertj.core.api.Assertions.assertThat;

import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.query.dao.PlaceSearchDao;
import com.sharehome.place.query.dto.PlaceSearchCondition;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Transactional
@DisplayName("PlaceServiceQuery 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PlaceSearchQueryTest {

    @Autowired
    PlaceSearchQuery placeSearchQuery;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    MemberRepository memberRepository;

    Member member;
    Place 채리호텔;
    Place 채리모텔;
    Place 인천펜션;
    Place 서울게하;

    @BeforeEach
    void setup() {
        member = 회원_Entity();
        memberRepository.save(member);
        채리호텔 = placeRepository.save(채리호텔_Entity(member));
        채리모텔 = placeRepository.save(채리모텔_Entity(member));
        인천펜션 = placeRepository.save(인천펜션_Entity(member));
        서울게하 = placeRepository.save(서울게하_Entity(member));
    }

    @Test
    void 숙소_이름으로_숙소_조회() {
        // given
        PlaceSearchCondition condition = new PlaceSearchCondition(
                "채리", null, null, null, null
        );

        // when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<PlaceSearchDao> result = placeSearchQuery.searchPlacesPage(condition, pageRequest);

        // then
        assertThat(result.getContent()).extracting("name")
                .containsExactly(채리호텔.getName(), 채리모텔.getName());

    }

    @Test
    void 게스트_인원수로_숙소_조회() {
        // given
        PlaceSearchCondition condition = new PlaceSearchCondition(
                null, null, 5, null, null
        );

        // when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<PlaceSearchDao> result = placeSearchQuery.searchPlacesPage(condition, pageRequest);

        // then
        assertThat(result.getContent()).extracting("name")
                .containsExactly(인천펜션.getName(), 서울게하.getName());
    }

    @Test
    void 여행지로_숙소_조회() {
        // given
        PlaceSearchCondition condition = new PlaceSearchCondition(
                null, "대전", null, null, null
        );

        // when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<PlaceSearchDao> result = placeSearchQuery.searchPlacesPage(condition, pageRequest);

        // then
        assertThat(result.getContent()).extracting("name")
                .containsExactly(채리호텔.getName(), 채리모텔.getName());
    }

    @Test
    void 체크인_체크아웃_날짜에_가능한_숙소_조회() {
        // given
        PlaceSearchCondition condition = new PlaceSearchCondition(
                null, null, null,
                LocalDate.of(2025, 8, 1),
                LocalDate.of(2025, 8, 15)
        );

        채리호텔.addUnavailableDate(List.of(
                LocalDate.of(2025, 8, 16)
        ));

        인천펜션.addUnavailableDate(List.of(
                LocalDate.of(2025, 8, 3)
        ));

        서울게하.addUnavailableDate(List.of(
                LocalDate.of(2025, 8, 15)
        ));

        placeRepository.flush();
        // when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<PlaceSearchDao> result = placeSearchQuery.searchPlacesPage(condition, pageRequest);

        // then
        assertThat(result.getContent()).extracting("name")
                .containsExactly(채리호텔.getName(), 채리모텔.getName());
    }

    @Test
    void 여러_조건으로_숙소_조회() {
        // given
        PlaceSearchCondition condition = new PlaceSearchCondition(
                "호텔",
                "대전",
                4,
                LocalDate.of(2025, 8, 1),
                LocalDate.of(2025, 8, 14)
        );

        인천펜션.addUnavailableDate(List.of(
                LocalDate.of(2025, 8, 3)
        ));

        서울게하.addUnavailableDate(List.of(
                LocalDate.of(2025, 8, 15)
        ));

        placeRepository.flush();
        // when
        PageRequest pageRequest = PageRequest.of(0, 4);
        Page<PlaceSearchDao> result = placeSearchQuery.searchPlacesPage(condition, pageRequest);

        // then
        assertThat(result.getContent()).extracting("name")
                .containsExactly(채리호텔.getName());
    }
}