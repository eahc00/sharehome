package com.sharehome.place.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Amenities {

    private Boolean hasWifi;
    private Boolean hasTv;
    private Boolean hasKitchen;
    private Boolean hasWasher;
    private Boolean hasParking;
    private Boolean hasPool;
    private Boolean hasBarbecue;

    @Builder
    public Amenities(
            Boolean hasWifi,
            Boolean hasTv,
            Boolean hasKitchen,
            Boolean hasWasher,
            Boolean hasParking,
            Boolean hasPool,
            Boolean hasBarbecue
    ) {
        this.hasWifi = hasWifi;
        this.hasTv = hasTv;
        this.hasKitchen = hasKitchen;
        this.hasWasher = hasWasher;
        this.hasParking = hasParking;
        this.hasPool = hasPool;
        this.hasBarbecue = hasBarbecue;
    }
}
