package com.sharehome.place.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@AllArgsConstructor
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
}
