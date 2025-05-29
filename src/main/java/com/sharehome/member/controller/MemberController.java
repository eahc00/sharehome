package com.sharehome.member.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(CREATED)
    @PostMapping
    public void signup(
            @RequestBody @Valid SignupRequest request
    ) {
        memberService.join(
                request.getEmail(),
                request.getName(),
                request.getBirth(),
                request.getPassword()
        );
    }
}
