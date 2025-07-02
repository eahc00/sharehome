package com.sharehome.place.controller.request;

import com.sharehome.common.domain.Address;
import com.sharehome.place.domain.Amenities;
import com.sharehome.place.domain.PlaceDetailType;
import com.sharehome.place.domain.PlaceType;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Builder
public record PlaceRegisterRequest(

        @Length(min = 2, max = 50)
        @NotNull(message = "숙소 이름은 null일 수 없습니다")
        String name,

        @NotNull(message = "숙소 유형은 null일 수 없습니다")
        PlaceType placeType,

        @NotNull(message = "숙소 세부 유형은 null일 수 없습니다")
        PlaceDetailType placeDetailType,

        @NotNull(message = "주소는 null일 수 없습니다")
        String city,

        @NotNull(message = "주소는 null일 수 없습니다")
        String street,

        @NotNull(message = "주소는 null일 수 없습니다")
        String zipcode,

        @NotNull(message = "기본 게스트 인원은 null일 수 없습니다")
        Integer guestCount,

        @NotNull(message = "최대 게스트 인원은 null일 수 없습니다")
        Integer maxGuestCount,

        @NotNull(message = "침실 수는 null일 수 없습니다")
        Integer bedroomCount,

        @NotNull(message = "침대 수는 null일 수 없습니다")
        Integer bedCount,

        @NotNull(message = "화장실 수는 null일 수 없습니다")
        Integer bathroomCount,

        @NotNull(message = "주중 기본 요금은 null일 수 없습니다.")
        Long weekdayPrice,

        @NotNull(message = "주말 기본 요금은 null일 수 없습니다.")
        Long weekendPrice,

        @NotNull(message = "체크인 시간은 null일 수 없습니다.")
        @Range(min = 0, max = 23)
        Integer checkInTime,

        @NotNull(message = "체크아웃 시간은 null일 수 없습니다.")
        @Range(min = 0, max = 23)
        Integer checkOutTime,

        @Length(max = 500)
        String detailInfo,

        Boolean hasWifi,
        Boolean hasTv,
        Boolean hasKitchen,
        Boolean hasWasher,
        Boolean hasParking,
        Boolean hasPool,
        Boolean hasBarbecue
) {
    public PlaceRegisterCommand toCommand(Long memberId) {
        Amenities amenities = Amenities.builder()
                .hasWifi(hasWifi)
                .hasTv(hasTv)
                .hasKitchen(hasKitchen)
                .hasWasher(hasWasher)
                .hasParking(hasParking)
                .hasPool(hasPool)
                .hasBarbecue(hasBarbecue)
                .build();

        return new PlaceRegisterCommand(
                memberId,
                name,
                placeType,
                placeDetailType,
                new Address(city, street, zipcode),
                guestCount,
                maxGuestCount,
                bedroomCount,
                bedCount,
                bathroomCount,
                weekdayPrice,
                weekendPrice,
                detailInfo,
                LocalTime.of(checkInTime, 0, 0),
                LocalTime.of(checkOutTime, 0, 0),
                amenities
        );
    }
}
