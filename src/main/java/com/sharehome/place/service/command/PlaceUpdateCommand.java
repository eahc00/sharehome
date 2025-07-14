package com.sharehome.place.service.command;

import com.sharehome.place.domain.Amenities;
import java.time.LocalTime;

public record PlaceUpdateCommand(
        Long memberId,
        Long placeId,
        String name,
        Integer bedCount,
        Integer bedroomCount,
        String detailInfo,
        Long weekdayPrice,
        Long weekendPrice,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        Amenities amenities
) {
}
