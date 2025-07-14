package com.sharehome.place.service;

import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import com.sharehome.place.service.command.PlaceUpdateCommand;
import com.sharehome.place.service.command.UnavailableDateDeleteCommand;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    public Long register(PlaceRegisterCommand command) {
        Member member = memberRepository.getById(command.memberId());

        Place place = command.toPlace(member);
        return placeRepository.save(place)
                .getId();
    }

    @Transactional
    public void updateUnavailableDate(UnavailableDateUpdateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Place place = placeRepository.getById(command.placeId());

        place.addUnavailableDate(member, command.unavailableDates());
    }

    @Transactional
    public void deleteUnavailableDate(UnavailableDateDeleteCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Place place = placeRepository.getById(command.placeId());

        place.removeUnavailableDate(member, command.unavailableDates());
    }

    public Place getPlace(Long placeId) {
        return placeRepository.getById(placeId);
    }

    @Transactional
    public void updatePlace(PlaceUpdateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Place place = placeRepository.getById(command.placeId());

        place.changePlaceInfo(
                member,
                command.name(),
                command.bedCount(),
                command.bedroomCount(),
                command.detailInfo(),
                command.weekdayPrice(),
                command.weekendPrice(),
                command.checkInTime(),
                command.checkOutTime(),
                command.amenities()
        );
    }
}
