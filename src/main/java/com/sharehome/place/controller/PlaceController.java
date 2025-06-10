package com.sharehome.place.controller;

import com.sharehome.common.auth.Auth;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.place.service.PlaceService;
import com.sharehome.place.service.command.PlaceRegisterCommand;
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
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<Void> register(
            @Auth Long memberId,
            @RequestBody @Valid PlaceRegisterRequest request
    ) {
        PlaceRegisterCommand command = request.toCommand(memberId);
        Long placeId = placeService.register(command);
        return ResponseEntity.created(URI.create("/place/" + placeId)).build();
    }
}
