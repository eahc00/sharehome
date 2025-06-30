package com.sharehome.review.controller.request;

import com.sharehome.review.service.command.ReviewCreateCommand;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record ReviewCreateRequest(
        @NotNull(message = "예약 id는 null이어서는 안됩니다.")
        Long reservationId,

        @NotNull(message = "별점은 null이어서는 안됩니다.")
        @Range(min = 0L, max = 5L)
        Integer stars,

        String content
) {

    public ReviewCreateCommand toCommand(Long memberId) {
        return new ReviewCreateCommand(
                memberId, reservationId, stars, content
        );
    }
}
