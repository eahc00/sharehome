package com.sharehome.place.query.dto;

import java.time.LocalDate;

public record PlaceSearchCondition(
        String name,
        String city,
        Integer guestCount,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
