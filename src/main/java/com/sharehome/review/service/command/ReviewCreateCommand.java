package com.sharehome.review.service.command;

public record ReviewCreateCommand(
        Long memberId,
        Long reservationId,
        Integer stars,
        String content
) {
}
