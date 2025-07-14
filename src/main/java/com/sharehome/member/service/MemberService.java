package com.sharehome.member.service;

import com.sharehome.common.exception.ConflictException;
import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.member.service.command.ChangePasswordCommand;
import com.sharehome.member.service.command.SignupCommand;
import com.sharehome.member.service.command.UpdateMemberCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(SignupCommand command) {
        memberRepository.findByEmail(command.email())
                .ifPresent(it -> {
                    throw new ConflictException("해당 이메일로 이미 가입한 회원이 있습니다");
                });
        Member member = new Member(
                command.email(),
                command.name(),
                command.birth(),
                command.password()
        );
        return memberRepository.save(member)
                .getId();
    }

    public Long login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new UnauthorizedException("이메일 혹은 비밀번호가 잘못되어 로그인에 실패하였습니다"));
        member.login(password);
        return member.getId();
    }

    @Transactional
    public void updateMember(UpdateMemberCommand command) {
        Member member = memberRepository.getById(command.memberId());
        member.update(command.nickname(), command.address());
    }

    @Transactional
    public void changePassword(ChangePasswordCommand command) {
        Member member = memberRepository.getById(command.memberId());
        member.changePassword(command.oldPassword(), command.newPassword());
    }
}
