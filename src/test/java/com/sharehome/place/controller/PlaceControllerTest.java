package com.sharehome.place.controller;

import static com.sharehome.place.domain.PlaceDetailType.ALL_SPACE;
import static com.sharehome.place.domain.PlaceType.RESIDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharehome.member.service.MemberService;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.place.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlaceController.class)
@DisplayName("PlaceController 은(는)")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class PlaceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PlaceService placeService;

    @MockitoBean
    private MemberService memberService;

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
                    .bedroomCount(1)
                    .bedCount(1)
                    .bathroomCount(1)
                    .weekdayPrice(50_000L)
                    .weekendPrice(70_000L)
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/place")
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
                    .bedroomCount(1)
                    .bedCount(1)
                    .bathroomCount(1)
                    .weekdayPrice(50_000L)
                    .weekendPrice(70_000L)
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/place")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}