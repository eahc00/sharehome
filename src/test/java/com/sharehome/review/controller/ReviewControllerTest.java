package com.sharehome.review.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharehome.common.auth.SessionService;
import com.sharehome.review.controller.request.ReviewCreateRequest;
import com.sharehome.review.service.ReviewService;
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

@WebMvcTest(ReviewController.class)
@DisplayName("ReviewController 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReviewService reviewService;

    @MockitoBean
    SessionService sessionService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class 리뷰_작성_시 {

        @Test
        void 작성_성공() throws Exception {
            // given
            ReviewCreateRequest request = new ReviewCreateRequest(
                    1L,
                    5,
                    "좋아요"
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // when&then
            mockMvc.perform(post("/reviews")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 3, 5})
        void 별점은_0점에서_5점_값이어야_한다(int stars) throws Exception {
            // given
            ReviewCreateRequest request = new ReviewCreateRequest(
                    1L,
                    stars,
                    "좋아요"
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // then
            mockMvc.perform(post("/reviews")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 6, 7})
        void 별점이_0점에서_5점_값이_아니면_예외(int stars) throws Exception {
            // given
            ReviewCreateRequest request = new ReviewCreateRequest(
                    1L,
                    stars,
                    "좋아요"
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // then
            mockMvc.perform(post("/reviews")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 별점이_없으면_예외() throws Exception {
            // given
            ReviewCreateRequest request = new ReviewCreateRequest(
                    1L,
                    null,
                    "좋아요"
            );

            MockHttpSession session = new MockHttpSession();
            session.setAttribute("memberId", 1L);

            // then
            mockMvc.perform(post("/reviews")
                            .session(session)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}