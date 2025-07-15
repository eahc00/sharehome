package com.sharehome.notification.service;

import static com.sharehome.fixture.MemberFixture.회원_Entity;

import com.sharehome.member.domain.MemberRepository;
import com.sharehome.notification.service.command.NotificationCreateCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("NotificationService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    MemberRepository memberRepository;

    Long memberId;

    @BeforeEach
    void setup() {
        memberId = memberRepository.save(회원_Entity())
                .getId();
    }

    @Test
    void 알림을_생성한다() {
        // given
        NotificationCreateCommand command = new NotificationCreateCommand(
                memberId,
                "예약일 전 알림입니다."
        );

        // when&then
        Assertions.assertDoesNotThrow(() ->
                notificationService.createNotification(command)
        );
    }
}