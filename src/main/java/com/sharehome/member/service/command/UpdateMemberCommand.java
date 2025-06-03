package com.sharehome.member.service.command;

import com.sharehome.member.domain.Address;

public record UpdateMemberCommand(
        Long memberId,
        String nickname,
        Address address
) {
}
