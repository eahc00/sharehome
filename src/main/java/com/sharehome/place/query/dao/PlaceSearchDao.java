package com.sharehome.place.query.dao;

import com.querydsl.core.annotations.QueryProjection;
import com.sharehome.common.domain.Address;
import java.time.DayOfWeek;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PlaceSearchDao {
    Long id;
    String name;
    Integer bedCount;
    Address address;
    Long weekdayPrice;
    Long weekendPrice;

    @QueryProjection
    public PlaceSearchDao(
            Long id,
            String name,
            Integer bedCount,
            Address address,
            Long weekdayPrice,
            Long weekendPrice
    ) {
        this.id = id;
        this.name = name;
        this.bedCount = bedCount;
        this.address = address;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    public Long getTodayPrice() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return switch (dayOfWeek) {
            case SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY -> weekdayPrice;
            case FRIDAY, SATURDAY -> weekendPrice;
        };
    }
}
