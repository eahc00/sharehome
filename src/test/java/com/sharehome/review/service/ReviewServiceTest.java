package com.sharehome.review.service;

import static com.sharehome.fixture.MemberFixture.회원_Entity;
import static com.sharehome.fixture.PlaceFixture.숙소_Entity;
import static com.sharehome.fixture.ReservationFixture.예약_Entity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.reservation.domain.ReservationRepository;
import com.sharehome.review.domain.ReviewRepository;
import com.sharehome.review.service.command.ReviewCreateCommand;
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
@DisplayName("ReviewService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Nested
    class 리뷰_작성_시 {

        private Long memberId;
        private Long reservationId;

        @BeforeEach
        void setup() {
            Member savedMember = memberRepository.save(회원_Entity());
            memberId = savedMember.getId();
            Place savedPlace = placeRepository.save(숙소_Entity());
            reservationId = reservationRepository.save(예약_Entity(savedPlace, savedMember)).getId();
        }

        @Test
        void 작성_성공() {
            // given
            ReviewCreateCommand command = new ReviewCreateCommand(
                    memberId, reservationId, 5, "좋아요"
            );

            // when&then
            Assertions.assertDoesNotThrow(() -> {
                reviewService.createReview(command);
            });
        }

        @Test
        void 해당_예약이_존재하지_않으면_예외() {
            // given
            ReviewCreateCommand command = new ReviewCreateCommand(
                    memberId, 100L, 5, "좋아요"
            );

            // when
            assertThatThrownBy(() ->
                    reviewService.createReview(command)
            ).isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("해당 id를 가진 예약이 없습니다.");
        }
    }
}