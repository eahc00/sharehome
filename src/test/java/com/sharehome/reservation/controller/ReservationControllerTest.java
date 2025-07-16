package com.sharehome.reservation.controller;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharehome.common.exception.ConflictException;
import com.sharehome.notification.service.NotificationService;
import com.sharehome.reservation.controller.request.ReservePlaceRequest;
import com.sharehome.reservation.service.ReservationService;
import com.sharehome.reservation.service.command.ReservePlaceCommand;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
@DisplayName("ReservationController 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReservationService reservationService;

    @MockitoBean
    NotificationService notificationService;

    @MockitoBean
    SessionService sessionService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 예약_생성_시 {

        @Test
        void 예약_성공() throws Exception {
            // given
            ReservePlaceRequest request = new ReservePlaceRequest(
                    1L,
                    LocalDate.now().plusMonths(1),
                    LocalDate.now().plusMonths(2),
                    4
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            mockMvc.perform(post("/reservation")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "20021009", "20000907", "19991111"
        })
        void 과거_날짜로_예약_시_예외(String date) throws Exception {
            // given
            LocalDate checkInDate = LocalDate.parse(date, BASIC_ISO_DATE);

            ReservePlaceRequest request = new ReservePlaceRequest(
                    1L,
                    checkInDate,
                    LocalDate.now().plusMonths(2),
                    4
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/reservation")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 체크아웃_날짜가_체크인_날짜보다_과거이면_예외() throws Exception {
            // given
            LocalDate checkInDate = LocalDate.now().plusMonths(2);
            LocalDate checkOutDate = LocalDate.now().plusMonths(1);

            ReservePlaceRequest request = new ReservePlaceRequest(
                    1L,
                    LocalDate.now().plusMonths(1),
                    LocalDate.now().plusMonths(1),
                    4
            );

            String content = objectMapper.writeValueAsString(request)
                    .replaceFirst(getStringFromLocalDate(checkOutDate), getStringFromLocalDate(checkInDate));

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/reservation")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 체크아웃_날짜가_1년_이후이면_예외() throws Exception {
            // given
            LocalDate checkOutDate = LocalDate.now().plusMonths(13);

            ReservePlaceRequest request = new ReservePlaceRequest(
                    1L,
                    LocalDate.now().plusMonths(1),
                    LocalDate.now().plusMonths(2),
                    4
            );

            String content = objectMapper.writeValueAsString(request)
                    .replaceFirst(getStringFromLocalDate(LocalDate.now().plusMonths(2)),
                            getStringFromLocalDate(checkOutDate));

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/reservation")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 예약_불가능일_예약_시_예외() throws Exception {
            // given
            ReservePlaceRequest request = new ReservePlaceRequest(
                    1L,
                    LocalDate.of(2025, 8, 14),
                    LocalDate.of(2025, 8, 16),
                    4
            );

            given(reservationService.reservePlace(any(ReservePlaceCommand.class)))
                    .willThrow(new ConflictException("예약 불가능한 날짜입니다."));

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/reservation")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        private static String getStringFromLocalDate(LocalDate localDate) {
            return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }
}