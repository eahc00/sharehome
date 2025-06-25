package com.sharehome.reservation.controller;

import com.sharehome.common.auth.Auth;
import com.sharehome.reservation.controller.request.ReservePlaceRequest;
import com.sharehome.reservation.service.ReservationService;
import com.sharehome.reservation.service.command.ReservePlaceCommand;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> reservePlace(
            @Auth Long memberId,
            @RequestBody @Valid ReservePlaceRequest request
    ) {
        ReservePlaceCommand command = request.toCommand(memberId);
        Long reservationId = reservationService.reservePlace(command);
        return ResponseEntity.created(URI.create("/reservation/" + reservationId)).build();
    }
}
