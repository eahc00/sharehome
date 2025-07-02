package com.sharehome.place.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.sharehome.common.domain.Address;
import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.member.domain.Member;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Enumerated(STRING)
    private PlaceType type;

    @Enumerated(STRING)
    private PlaceDetailType detailType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "place_city")),
            @AttributeOverride(name = "street", column = @Column(name = "place_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "place_zipcode"))
    })
    private Address address;

    @Column(nullable = false)
    private Integer guestCount;
    private Integer maxGuestCount;

    @Column(nullable = false)
    private Integer bedroomCount;

    @Column(nullable = false)
    private Integer bedCount;

    @Column(nullable = false)
    private Integer bathroomCount;

    @Column(nullable = false)
    private Long weekdayPrice;

    @Column(nullable = false)
    private Long weekendPrice;

    @OneToMany(mappedBy = "place", cascade = ALL)
    private List<PlaceImage> images = new ArrayList<>();

    private String detailInfo;

    @Embedded
    private Amenities amenities;

    private List<LocalDate> unavailableDate = new ArrayList<>();

    @Builder
    public Place(
            Long id,
            Member member,
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
            List<PlaceImage> images,
            String detailInfo,
            Amenities amenities
    ) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.type = type;
        this.detailType = detailType;
        this.address = address;
        this.guestCount = guestCount;
        this.maxGuestCount = maxGuestCount;
        this.bedroomCount = bedroomCount;
        this.bedCount = bedCount;
        this.bathroomCount = bathroomCount;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
        this.images = images;
        this.detailInfo = detailInfo;
        this.amenities = amenities;
    }

    public void validateMember(Member member) {
        if (!this.member.getId().equals(member.getId())) {
            throw new UnauthorizedException("인가되지 않은 사용자입니다");
        }
    }

    public void addUnavailableDate(Member member, List<LocalDate> localDates) {
        validateMember(member);
        unavailableDate.addAll(localDates);
        unavailableDate = unavailableDate.stream().distinct().collect(Collectors.toList());
    }
}
