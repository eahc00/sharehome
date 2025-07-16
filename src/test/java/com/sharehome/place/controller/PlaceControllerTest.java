package com.sharehome.place.controller;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.불가능일_삭제_request;
import static com.sharehome.fixture.PlaceFixture.불가능일_설정_request;
import static com.sharehome.fixture.PlaceFixture.숙소_수정_request;
import static com.sharehome.fixture.PlaceFixture.채리호텔_Entity;
import static com.sharehome.place.domain.PlaceDetailType.ALL_SPACE;
import static com.sharehome.place.domain.PlaceType.RESIDENCE;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharehome.common.auth.SessionService;
import com.sharehome.common.domain.Address;
import com.sharehome.member.domain.Member;
import com.sharehome.member.service.MemberService;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.place.controller.request.PlaceSearchRequest;
import com.sharehome.place.controller.request.PlaceUpdateRequest;
import com.sharehome.place.controller.request.UnavailableDateDeleteRequest;
import com.sharehome.place.controller.request.UnavailableDateUpdateRequest;
import com.sharehome.place.domain.Place;
import com.sharehome.place.query.PlaceSearchQuery;
import com.sharehome.place.query.dao.PlaceSearchDao;
import com.sharehome.place.service.PlaceService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlaceController.class)
@DisplayName("PlaceController 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PlaceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PlaceService placeService;

    @MockitoBean
    MemberService memberService;

    @MockitoBean
    PlaceSearchQuery placeSearchQuery;

    @MockitoBean
    SessionService sessionService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 숙소_등록_시 {

        @ParameterizedTest
        @ValueSource(ints = {
                2, 10, 25, 50
        })
        void 숙소_이름은_2자_이상_50자_이하여야_한다(int length) throws Exception {
            // given
            String name = "a".repeat(length);
            PlaceRegisterRequest request = PlaceRegisterRequest.builder()
                    .name(name)
                    .placeType(RESIDENCE)
                    .placeDetailType(ALL_SPACE)
                    .city("대전")
                    .street("대학로")
                    .zipcode("12345")
                    .guestCount(2)
                    .maxGuestCount(4)
                    .bedroomCount(1)
                    .bedCount(1)
                    .bathroomCount(1)
                    .weekdayPrice(50_000L)
                    .weekendPrice(70_000L)
                    .checkInTime(18)
                    .checkOutTime(11)
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/places")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1, 51, 100
        })
        void 숙소_이름이_2자_이상_50자_이하가_아니면_예외(int length) throws Exception {
            // given
            String name = "a".repeat(length);
            PlaceRegisterRequest request = PlaceRegisterRequest.builder()
                    .name(name)
                    .placeType(RESIDENCE)
                    .placeDetailType(ALL_SPACE)
                    .city("대전")
                    .street("대학로")
                    .zipcode("12345")
                    .guestCount(2)
                    .maxGuestCount(4)
                    .bedroomCount(1)
                    .bedCount(1)
                    .bathroomCount(1)
                    .weekdayPrice(50_000L)
                    .weekendPrice(70_000L)
                    .checkInTime(18)
                    .checkOutTime(11)
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/places")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(ints = {
                0, 5, 15, 23
        })
        void 체크인_시간이_0부터_23시까지_허용된다(int hour) throws Exception {
            // given
            PlaceRegisterRequest request = PlaceRegisterRequest.builder()
                    .name("채리호텔")
                    .placeType(RESIDENCE)
                    .placeDetailType(ALL_SPACE)
                    .city("대전")
                    .street("대학로")
                    .zipcode("12345")
                    .guestCount(2)
                    .maxGuestCount(4)
                    .bedroomCount(1)
                    .bedCount(1)
                    .bathroomCount(1)
                    .weekdayPrice(50_000L)
                    .weekendPrice(70_000L)
                    .checkInTime(hour)
                    .checkOutTime(11)
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/places")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @ValueSource(ints = {
                -1, 24, 100
        })
        void 체크인_시간이_0부터_23시까지가_아니면_예외(int hour) throws Exception {
            // given
            PlaceRegisterRequest request = PlaceRegisterRequest.builder()
                    .name("채리호텔")
                    .placeType(RESIDENCE)
                    .placeDetailType(ALL_SPACE)
                    .city("대전")
                    .street("대학로")
                    .zipcode("12345")
                    .guestCount(2)
                    .maxGuestCount(4)
                    .bedroomCount(1)
                    .bedCount(1)
                    .bathroomCount(1)
                    .weekdayPrice(50_000L)
                    .weekendPrice(70_000L)
                    .checkInTime(hour)
                    .checkOutTime(11)
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/places")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 숙소_예약불가일 {

        @Test
        void 설정_성공() throws Exception {
            // given
            UnavailableDateUpdateRequest request = 불가능일_설정_request();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/places/1/unavailable-date")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNoContent());
        }

        @Test
        void 설정시_과거의_날짜가_있으면_예외() throws Exception {
            // given
            UnavailableDateUpdateRequest request = new UnavailableDateUpdateRequest(
                    List.of(
                            LocalDate.now().plusMonths(1),
                            LocalDate.now().minusMonths(1)
                    )
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/places/1/unavailable-date")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 제거_성공() throws Exception {
            // given
            UnavailableDateDeleteRequest request = 불가능일_삭제_request();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(delete("/places/1/unavailable-date")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class 숙소_조회_시 {

        @Test
        void 조회_성공() throws Exception {
            // given
            Member member = 회원_Entity();
            Place place = 채리호텔_Entity(member);
            setField(place, "id", 1L);
            given(placeService.getPlace(1L))
                    .willReturn(place);

            // when&then
            mockMvc.perform(get("/places/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("채리호텔"));

        }

        @Test
        void 조건에_맞는_조회_성공() throws Exception {
            // given
            List<PlaceSearchDao> content = List.of(
                    new PlaceSearchDao(
                            1L,
                            "채리호텔",
                            2,
                            new Address("대전", null, null),
                            50_000L,
                            70_000L
                    ),
                    new PlaceSearchDao(
                            2L,
                            "채리서울게하",
                            2,
                            new Address("서울", null, null),
                            70_000L,
                            90_000L
                    )
            );
            PageRequest pageable = PageRequest.of(0, 4);
            given(placeSearchQuery.searchPlacesPage(any(), any()))
                    .willReturn(PageableExecutionUtils.getPage(content, pageable, content::size));

            PlaceSearchRequest request = new PlaceSearchRequest(
                    "채리", null, 4, null, null
            );

            // when&then
            mockMvc.perform(get("/places")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("채리호텔"))
                    .andExpect(jsonPath("$.content[1].name").value("채리서울게하"));
        }
    }

    @Test
    void 숙소를_수정한다() throws Exception {
        // given
        PlaceUpdateRequest request = 숙소_수정_request();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", 1L);

        // when & then
        mockMvc.perform(put("/places/1")
                        .session(session)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }
}