package com.sharehome.fixture;

import static com.sharehome.place.domain.PlaceDetailType.ALL_SPACE;
import static com.sharehome.place.domain.PlaceType.RESIDENCE;

import com.sharehome.common.domain.Address;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.place.domain.Amenities;
import com.sharehome.place.domain.Place;
import com.sharehome.place.service.command.PlaceRegisterCommand;

public class PlaceFixture {

    public static Place 숙소_Entity() {
        return Place.builder()
                .name("채리호텔")
                .type(RESIDENCE)
                .detailType(ALL_SPACE)
                .address(new Address("대전", "대학로", "12345"))
                .guestCount(2)
                .maxGuestCount(4)
                .bedroomCount(1)
                .bedCount(1)
                .bathroomCount(1)
                .weekdayPrice(50_000L)
                .weekendPrice(70_000L)
                .build();
    }

    public static Place 숙소_Entity2() {
        return Place.builder()
                .name("채리호텔2")
                .type(RESIDENCE)
                .detailType(ALL_SPACE)
                .address(new Address("대전", "대학로", "12345"))
                .guestCount(2)
                .maxGuestCount(4)
                .bedroomCount(1)
                .bedCount(1)
                .bathroomCount(1)
                .weekdayPrice(50_000L)
                .weekendPrice(70_000L)
                .build();
    }

    public static PlaceRegisterRequest 숙소_등록_request() {
        return PlaceRegisterRequest.builder()
                .name("채리호텔")
                .placeType(RESIDENCE)
                .placeDetailType(ALL_SPACE)
                .city("대전")
                .street("대학로")
                .zipcode("12345")
                .guestCount(2)
                .maxGuestCount(4)
                .bedroomCount(1)
                .bedCount(1)
                .bathroomCount(1)
                .weekdayPrice(50_000L)
                .weekendPrice(70_000L)
                .build();
    }

    public static PlaceRegisterCommand 숙소_등록_command(Long memberId) {
        return PlaceRegisterCommand.builder()
                .memberId(memberId)
                .name("채리호텔")
                .placeType(RESIDENCE)
                .placeDetailType(ALL_SPACE)
                .address(new Address("대전", "대학로", "12345"))
                .guestCount(2)
                .maxGuestCount(4)
                .bedroomCount(1)
                .bedCount(1)
                .bathroomCount(1)
                .weekdayPrice(50_000L)
                .weekendPrice(70_000L)
                .amenities(Amenities.builder().build())
                .build();
    }
}
