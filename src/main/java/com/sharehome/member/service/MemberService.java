package com.sharehome.member.service;

import com.sharehome.common.exception.ConflictException;
import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.member.service.command.MemberCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(MemberCommand command) {
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
}
