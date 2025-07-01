package com.sharehome.reservation.controller.request;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sharehome.common.exception.BadRequestException;
import com.sharehome.reservation.service.command.ReservePlaceCommand;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservePlaceRequest(
        @NotNull(message = "숙소 id는 null이어서는 안됩니다.")
        Long placeId,

        @FutureOrPresent(message = "과거의 날짜는 불가능합니다.")
        @NotNull(message = "체크인 날짜는 null이어서는 안됩니다.")
        @JsonFormat(shape = STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        LocalDate checkInDate,

        @FutureOrPresent(message = "과거의 날짜는 불가능합니다.")
        @NotNull(message = "체크아웃 날짜는 null이어서는 안됩니다.")
        @JsonFormat(shape = STRING, pattern = "yyyyMMdd", timezone = "Asia/Seoul")
        LocalDate checkOutDate,

        @NotNull(message = "게스트 인원수는 null이어서는 안됩니다.")
        Integer guestCount
) {
    public ReservePlaceRequest {
        validateCheckOutDate(checkInDate, checkOutDate);
    }

    public ReservePlaceCommand toCommand(Long memberId) {
        return new ReservePlaceCommand(
                memberId, placeId, checkInDate, checkOutDate, guestCount
        );
    }

    public void validateCheckOutDate(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkOutDate.isAfter(LocalDate.now().plusYears(1))) {
            throw new BadRequestException("1년 후의 날짜까지만 예약이 가능합니다.");
        }

        if (checkOutDate.isBefore(checkInDate)) {
            throw new BadRequestException("체크아웃 날짜가 체크인 날짜보다 과거일 수 없습니다.");
        }
    }
}
