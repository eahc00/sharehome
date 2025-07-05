package com.sharehome.place.controller.response;

import com.sharehome.common.domain.Address;
import com.sharehome.place.domain.Amenities;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceDetailType;
import com.sharehome.place.domain.PlaceType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PlaceResponse(
        String name,
        PlaceType type,
        PlaceDetailType detailType,
        Address address,
        Integer guestCount,
        Integer maxGuestCount,
        Integer bedroomCount,
        Integer bedCount,
        Integer bathroomCount,
        Long weekdayPrice,
        Long weekendPrice,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String detailInfo,
        Amenities amenities,
        List<LocalDate> unavailableDate
) {

    public static PlaceResponse fromPlace(Place place) {
        return new PlaceResponse(
                place.getName(),
                place.getType(),
                place.getDetailType(),
                place.getAddress(),
                place.getGuestCount(),
                place.getMaxGuestCount(),
                place.getBedroomCount(),
                place.getBedCount(),
                place.getBathroomCount(),
                place.getWeekdayPrice(),
                place.getWeekendPrice(),
                place.getCheckInTime(),
                place.getCheckOutTime(),
                place.getDetailInfo(),
                place.getAmenities(),
                place.getUnavailableDate()
        );
    }
}
