package com.sharehome.place.controller.response;

import com.sharehome.place.query.dao.PlacesSearchDao;

public record PlacesSearchResponse(
        Long id,
        String name,
        Integer bedCount,
        String city,
        Long todayPrice
) {

    public PlacesSearchResponse fromPlacesSearchDto(PlacesSearchDao dto) {
        return new PlacesSearchResponse(
                dto.getId(),
                dto.getName(),
                dto.getBedCount(),
                dto.getAddress().getCity(),
                dto.getTodayPrice()
        );
    }
}
