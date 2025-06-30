package com.sharehome.review.controller;

import com.sharehome.common.auth.Auth;
import com.sharehome.review.controller.request.ReviewCreateRequest;
import com.sharehome.review.service.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(
            @Auth Long memberId,
            @RequestBody @Valid ReviewCreateRequest request
    ) {
        Long reviewId = reviewService.createReview(request.toCommand(memberId));

        return ResponseEntity.created(URI.create("/reviews/" + reviewId)).build();
    }
}
