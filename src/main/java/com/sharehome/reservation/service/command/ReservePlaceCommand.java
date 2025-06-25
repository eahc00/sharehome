package com.sharehome.reservation.service.command;

import java.time.LocalDate;

public record ReservePlaceCommand(
        Long memberId,
        Long placeId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer guestCount
) {
}
