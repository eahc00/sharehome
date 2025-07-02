package com.sharehome.place.service.command;

import java.time.LocalDate;
import java.util.List;

public record UnavailableDateUpdateCommand(
        Long memberId,
        Long placeId,
        List<LocalDate> unavailableDates
) {
}
