package com.sharehome.place.controller.response;

import com.sharehome.place.query.dao.PlaceSearchDao;
import org.springframework.data.domain.Page;

public record PlaceSearchResponse(
        Long id,
        String name,
        Integer bedCount,
        String city,
        Long todayPrice
) {

    public static Page<PlaceSearchResponse> from(Page<PlaceSearchDao> searchDaoPage) {
        return searchDaoPage.map(PlaceSearchResponse::fromPlaceSearchDao);
    }

    private static PlaceSearchResponse fromPlaceSearchDao(PlaceSearchDao dto) {
        return new PlaceSearchResponse(
                dto.getId(),
                dto.getName(),
                dto.getBedCount(),
                dto.getAddress().getCity(),
                dto.getTodayPrice()
        );
    }
}
