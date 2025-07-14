package com.sharehome.place.controller.request;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sharehome.place.service.command.UnavailableDateDeleteCommand;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.List;

public record UnavailableDateDeleteRequest(
        @JsonFormat(shape = STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        List<@FutureOrPresent LocalDate> unavailableDates
) {

    public UnavailableDateDeleteCommand toCommand(Long memberId, Long placeId) {
        return new UnavailableDateDeleteCommand(memberId, placeId, unavailableDates);
    }
}