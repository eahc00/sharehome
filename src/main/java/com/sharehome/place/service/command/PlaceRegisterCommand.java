package com.sharehome.place.service.command;

import com.sharehome.common.domain.Address;
import com.sharehome.member.domain.Member;
import com.sharehome.place.domain.Amenities;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceDetailType;
import com.sharehome.place.domain.PlaceType;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record PlaceRegisterCommand(
        Long memberId,
        String name,
        PlaceType placeType,
        PlaceDetailType placeDetailType,
        Address address,
        Integer guestCount,
        Integer maxGuestCount,
        Integer bedroomCount,
        Integer bedCount,
        Integer bathroomCount,
        Long weekdayPrice,
        Long weekendPrice,
        String detailInfo,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        Amenities amenities
) {
    public Place toPlace(Member member) {
        return Place.builder()
                .name(name)
                .type(placeType)
                .detailType(placeDetailType)
                .address(address)
                .guestCount(guestCount)
                .maxGuestCount(maxGuestCount)
                .bedroomCount(bedroomCount)
                .bedCount(bedCount)
                .bathroomCount(bathroomCount)
                .weekdayPrice(weekdayPrice)
                .weekendPrice(weekendPrice)
                .detailInfo(detailInfo)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .amenities(amenities)
                .member(member)
                .build();
    }
}
