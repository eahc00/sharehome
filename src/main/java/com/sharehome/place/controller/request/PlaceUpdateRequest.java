package com.sharehome.place.controller.request;

import com.sharehome.place.domain.Amenities;
import com.sharehome.place.service.command.PlaceUpdateCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record PlaceUpdateRequest(

        @Length(min = 2, max = 50)
        @NotNull(message = "숙소 이름은 null일 수 없습니다")
        String name,

        @NotNull(message = "침대 수는 null일 수 없습니다")
        Integer bedCount,

        @NotNull(message = "화장실 수는 null일 수 없습니다")
        Integer bathroomCount,

        @Length(max = 500)
        String detailInfo,

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

        Boolean hasWifi,
        Boolean hasTv,
        Boolean hasKitchen,
        Boolean hasWasher,
        Boolean hasParking,
        Boolean hasPool,
        Boolean hasBarbecue
) {

    public PlaceUpdateCommand toCommand(Long memberId, Long placeId) {
        Amenities amenities = Amenities.builder()
                .hasWifi(hasWifi)
                .hasTv(hasTv)
                .hasKitchen(hasKitchen)
                .hasWasher(hasWasher)
                .hasParking(hasParking)
                .hasPool(hasPool)
                .hasBarbecue(hasBarbecue)
                .build();

        return new PlaceUpdateCommand(
                memberId,
                placeId,
                name,
                bedCount,
                bathroomCount,
                detailInfo,
                weekdayPrice,
                weekendPrice,
                LocalTime.of(checkInTime, 0, 0),
                LocalTime.of(checkOutTime, 0, 0),
                amenities
        );
    }

}
