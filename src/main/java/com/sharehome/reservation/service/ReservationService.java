package com.sharehome.reservation.service;

import com.sharehome.common.exception.ConflictException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.place.domain.Place;
import com.sharehome.place.domain.PlaceRepository;
import com.sharehome.reservation.domain.Reservation;
import com.sharehome.reservation.domain.ReservationRepository;
import com.sharehome.reservation.service.command.ReservePlaceCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;

    public Long reservePlace(ReservePlaceCommand command) {
        Place place = placeRepository.getById(command.placeId());
        Member member = memberRepository.getById(command.memberId());

        validateDuplicatedReservation(command, place);
        place.validateAvailableDate(command.checkInDate(), command.checkOutDate());
        place.validateGuestCount(command.guestCount());

        Reservation reservation = new Reservation(
                place, member, command.checkInDate(), command.checkOutDate(), command.guestCount()
        );
        return reservationRepository.save(reservation).getId();
    }

    private void validateDuplicatedReservation(ReservePlaceCommand command, Place place) {
        if (!reservationRepository.findAllByPlaceAndReservationDate(
                command.checkInDate(), command.checkOutDate(), place
        ).isEmpty()) {
            throw new ConflictException("해당 숙소에 예약 기간과 중복되는 예약이 존재합니다.");
        }
    }
}
