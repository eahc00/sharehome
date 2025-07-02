package com.sharehome.place.service;

import com.sharehome.common.exception.NotFoundException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    public Long register(PlaceRegisterCommand command) {
        Member member = memberRepository.findById(command.memberId()).orElseThrow(() ->
                new NotFoundException("해당 id를 가진 회원이 없습니다.")
        );
        Place place = command.toPlace(member);
        return placeRepository.save(place)
                .getId();
    }

    public void updateUnavailableDate(UnavailableDateUpdateCommand command) {
        Member member = memberRepository.findById(command.memberId()).orElseThrow(() ->
                new NotFoundException("해당 id를 가진 회원이 없습니다.")
        );
        Place place = placeRepository.findById(command.placeId()).orElseThrow(() ->
                new NotFoundException("해당 id를 가진 숙소가 없습니다.")
        );

        place.addUnavailableDate(member, command.unavailableDates());
    }
}
