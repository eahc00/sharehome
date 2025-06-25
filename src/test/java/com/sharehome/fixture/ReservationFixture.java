package com.sharehome.fixture;

import com.sharehome.reservation.controller.request.ReservePlaceRequest;
import com.sharehome.reservation.domain.Reservation;
import com.sharehome.reservation.service.command.ReservePlaceCommand;
import java.time.LocalDate;

public class ReservationFixture {

    public static ReservePlaceCommand 예약_command() {
        return new ReservePlaceCommand(
                1L,
                1L,
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 5),
                4
        );
    }

    public static Reservation 예약_Entity() {
        return new Reservation(
                PlaceFixture.숙소_Entity(),
                MemberFixture.회원_Entity(),
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 5),
                4
        );
    }

    public static ReservePlaceRequest 예약_request() {
        return new ReservePlaceRequest(
                1L,
                LocalDate.of(2100, 3, 1),
                LocalDate.of(2100, 3, 4),
                4
        );
    }
}
