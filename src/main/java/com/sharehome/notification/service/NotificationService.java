package com.sharehome.notification.service;

import com.sharehome.member.domain.Member;
import com.sharehome.member.domain.MemberRepository;
import com.sharehome.notification.domain.Notification;
import com.sharehome.notification.domain.NotificationRepository;
import com.sharehome.notification.service.command.NotificationCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public Long createNotification(NotificationCreateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Notification notification = new Notification(command.content(), member);
        return notificationRepository.save(notification)
                .getId();
    }
}
