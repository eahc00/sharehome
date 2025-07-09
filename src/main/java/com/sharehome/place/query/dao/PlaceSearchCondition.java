package com.sharehome.place.query.dao;

import java.time.LocalDate;

public record PlaceSearchCondition(
        String name,
        String city,
        Integer guestCount,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
