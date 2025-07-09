package com.sharehome.place.controller.request;

import com.sharehome.place.query.dto.PlaceSearchCondition;
import java.time.LocalDate;

public record PlaceSearchRequest(
        String name,
        String city,
        Integer guestCount,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {

    public PlaceSearchCondition toCondition() {
        return new PlaceSearchCondition(
                name, city, guestCount, checkInDate, checkOutDate
        );
    }
}
