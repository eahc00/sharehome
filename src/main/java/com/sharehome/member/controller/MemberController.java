package com.sharehome.member.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.sharehome.common.auth.Auth;
import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.controller.request.UpdateMemberRequest;
import com.sharehome.member.service.MemberService;
import com.sharehome.member.service.command.SignupCommand;
import com.sharehome.member.service.command.UpdateMemberCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        SignupCommand command = request.toCommand();
        memberService.join(command);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        Long loginMemberId = memberService.login(request.email(), request.password());
        HttpSession session = httpRequest.getSession();
        session.setAttribute("memberId", loginMemberId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/my")
    public void updateMember(
            @Auth Long memberId,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        UpdateMemberCommand command = request.toCommand(memberId);
        memberService.updateMember(command);
    }
}
