package com.sharehome.place.controller.request;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.List;

public record UnavailableDateUpdateRequest(

        @JsonFormat(shape = STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        List<@FutureOrPresent LocalDate> unavailableDates
) {

    public UnavailableDateUpdateCommand toCommand(Long memberId, Long placeId) {
        return new UnavailableDateUpdateCommand(memberId, placeId, unavailableDates);
    }
}
