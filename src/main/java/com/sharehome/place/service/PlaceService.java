package com.sharehome.place.service;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.domain.UnavailableDate;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import com.sharehome.place.service.command.PlaceUpdateCommand;
import com.sharehome.place.service.command.UnavailableDateDeleteCommand;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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

        place.validateMember(member);

        Set<LocalDate> unavailableDateValues = new HashSet<>(place.getUnavailableDateValues());
        command.unavailableDates().stream()
                .distinct()
                .filter(it -> !unavailableDateValues.contains(it))
                .forEach(it -> place.addUnavailableDate(new UnavailableDate(place, it)));
    }

    @Transactional
    public void deleteUnavailableDate(UnavailableDateDeleteCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Place place = placeRepository.getById(command.placeId());

        place.validateMember(member);

        Map<LocalDate, UnavailableDate> unavailableDateMap = place.getUnavailableDates().stream()
                .collect(Collectors.toMap(UnavailableDate::getDate, Function.identity()));

        for (LocalDate localDate : command.unavailableDates()) {
            if (!unavailableDateMap.containsKey(localDate)) {
                throw new NotFoundException("해당 날짜는 예약 불가능일에 존재하지 않습니다. : " + localDate);
            }
            UnavailableDate unavailableDate = unavailableDateMap.get(localDate);
            place.removeUnavailableDate(unavailableDate);
        }
    }

    public Place getPlace(Long placeId) {
        return placeRepository.getById(placeId);
    }

    @Transactional
    public void updatePlace(PlaceUpdateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Place place = placeRepository.getById(command.placeId());

        place.validateMember(member);
        place.changePlaceInfo(
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
