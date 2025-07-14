package com.sharehome.review.service;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.reservation.domain.Reservation;
import com.sharehome.reservation.domain.ReservationRepository;
import com.sharehome.review.domain.Review;
import com.sharehome.review.domain.ReviewRepository;
import com.sharehome.review.service.command.ReviewCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public Long createReview(ReviewCreateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Reservation reservation = reservationRepository.findById(command.reservationId()).orElseThrow(() -> {
            throw new NotFoundException("해당 id를 가진 예약이 없습니다.");
        });
        Review review = new Review(member, reservation, command.stars(), command.content());
        return reviewRepository.save(review)
                .getId();
    }
}
