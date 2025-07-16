package com.sharehome.member.controller;

import com.sharehome.common.auth.Auth;
import com.sharehome.common.auth.SessionService;
import com.sharehome.member.controller.request.ChangePasswordRequest;
import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.MemberUpdateRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.service.MemberService;
import com.sharehome.member.service.command.ChangePasswordCommand;
import com.sharehome.member.service.command.SignupCommand;
import com.sharehome.member.service.command.UpdateMemberCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<Void> signup(
            @RequestBody @Valid SignupRequest request
    ) {
        SignupCommand command = request.toCommand();
        Long memberId = memberService.join(command);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        Long loginMemberId = memberService.login(request.email(), request.password());
        sessionService.createSession(httpRequest, loginMemberId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/my")
    public ResponseEntity<Void> updateMember(
            @Auth Long memberId,
            @RequestBody @Valid MemberUpdateRequest request
    ) {
        UpdateMemberCommand command = request.toCommand(memberId);
        memberService.updateMember(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Auth Long memberId,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        ChangePasswordCommand command = request.toCommand(memberId);
        memberService.changePassword(command);
        return ResponseEntity.ok().build();
    }
}
