package com.sharehome.common.auth;

import com.sharehome.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class SessionService {

    private static final String MEMBER_ID_KEY = "memberId";

    public Long extractMemberId(NativeWebRequest webRequest) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new UnauthorizedException("세션 정보가 없습니다.");
        }

        return (Long) session.getAttribute(MEMBER_ID_KEY);
    }

    public static void createSession(HttpServletRequest httpRequest, Long loginMemberId) {
        HttpSession session = httpRequest.getSession();
        session.setAttribute(MEMBER_ID_KEY, loginMemberId);
    }
}
