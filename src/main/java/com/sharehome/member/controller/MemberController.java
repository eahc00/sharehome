package com.sharehome.member.controller;

import com.sharehome.common.auth.Auth;
import com.sharehome.member.controller.request.ChangePasswordRequest;
import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.controller.request.UpdateMemberRequest;
import com.sharehome.member.service.MemberService;
import com.sharehome.member.service.command.ChangePasswordCommand;
import com.sharehome.member.service.command.SignupCommand;
import com.sharehome.member.service.command.UpdateMemberCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @PutMapping("/change-password")
    public void changePassword(
            @Auth Long memberId,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        ChangePasswordCommand command = request.toCommand(memberId);
        memberService.changePassword(command);
    }
}
