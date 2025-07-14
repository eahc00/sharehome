package com.sharehome.place.controller;

import com.sharehome.common.auth.Auth;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.place.controller.request.PlaceSearchRequest;
import com.sharehome.place.controller.request.PlaceUpdateRequest;
import com.sharehome.place.controller.request.UnavailableDateUpdateRequest;
import com.sharehome.place.controller.response.PlaceResponse;
import com.sharehome.place.controller.response.PlaceSearchResponse;
import com.sharehome.place.domain.Place;
import com.sharehome.place.query.PlaceSearchQuery;
import com.sharehome.place.query.dao.PlaceSearchDao;
import com.sharehome.place.service.PlaceService;
import com.sharehome.place.service.command.PlaceRegisterCommand;
import com.sharehome.place.service.command.PlaceUpdateCommand;
import com.sharehome.place.service.command.UnavailableDateUpdateCommand;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceSearchQuery placeSearchQuery;

    @PostMapping
    public ResponseEntity<Void> register(
            @Auth Long memberId,
            @RequestBody @Valid PlaceRegisterRequest request
    ) {
        PlaceRegisterCommand command = request.toCommand(memberId);
        Long placeId = placeService.register(command);
        return ResponseEntity.created(URI.create("/places/" + placeId)).build();
    }

    @PostMapping("/{placeId}/unavailable-date")
    public ResponseEntity<Void> updateUnavailableDate(
            @PathVariable Long placeId,
            @Auth Long memberId,
            @RequestBody @Valid UnavailableDateUpdateRequest request
    ) {
        UnavailableDateUpdateCommand command = request.toCommand(memberId, placeId);
        placeService.updateUnavailableDate(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceResponse> getPlace(@PathVariable Long placeId) {
        Place place = placeService.getPlace(placeId);
        PlaceResponse response = PlaceResponse.fromPlace(place);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<PlaceSearchResponse> placesSearch(
            @RequestBody PlaceSearchRequest request,
            Pageable pageable
    ) {
        Page<PlaceSearchDao> queryResult = placeSearchQuery.searchPlacesPage(request.toCondition(), pageable);
        return PlaceSearchResponse.from(queryResult);
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<Void> updatePlaceInfo(
            @PathVariable Long placeId,
            @Auth Long memberId,
            @RequestBody @Valid PlaceUpdateRequest request
    ) {
        PlaceUpdateCommand command = request.toCommand(memberId, placeId);
        placeService.updatePlace(command);
        return ResponseEntity.ok().build();
    }
}
