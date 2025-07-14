package com.sharehome.member.controller.request;

import com.sharehome.common.domain.Address;
import com.sharehome.member.service.command.UpdateMemberCommand;

public record MemberUpdateRequest(
        String nickname,
        String city,
        String street,
        String zipcode
) {

    public Address toAddress() {
        return new Address(city, street, zipcode);
    }

    public UpdateMemberCommand toCommand(Long memberId) {
        return new UpdateMemberCommand(
                memberId,
                nickname,
                new Address(city, street, zipcode)
        );
    }
}
