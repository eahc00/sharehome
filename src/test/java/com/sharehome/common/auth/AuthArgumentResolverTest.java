package com.sharehome.common.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sharehome.common.exception.UnauthorizedException;
import com.sharehome.member.controller.MemberController;
import com.sharehome.member.controller.request.UpdateMemberRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthArgumentResolverTest {

    private final AuthArgumentResolver authArgumentResolver = new AuthArgumentResolver();

    @Mock
    private MemberController memberController;

    @Test
    void 지원되는_파라미터이면_true를_반환한다() throws NoSuchMethodException {
        // given
        MethodParameter parameter = createUpdateMemberMethodParameter(0);

        // when
        boolean isSupported = authArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(isSupported).isTrue();
    }

    @Test
    void 지원되는_파라미터가_아니면_false를_반환한다() throws NoSuchMethodException {
        // given
        MethodParameter parameter = createUpdateMemberMethodParameter(1);

        // when
        boolean isSupported = authArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(isSupported).isFalse();
    }

    @Test
    void 지원되는_파라미터이면_세션ID로_멤버ID_반환() throws Exception {
        // given
        MethodParameter parameter = createUpdateMemberMethodParameter(0);
        MockHttpServletRequest loginServletRequest = new MockHttpServletRequest();
        HttpSession session = loginServletRequest.getSession(true);
        session.setAttribute("memberId", 1L);

        // when
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("PUT", "/members/my");
        servletRequest.setSession(session);
        Long memberId = authArgumentResolver.resolveArgument(
                parameter,
                null,
                new ServletWebRequest(servletRequest),
                null
        );

        // then
        assertThat(memberId).isEqualTo(1L);
    }


    @Test
    void 세션_졍보가_없으면_예외() throws Exception {
        // given
        MethodParameter parameter = createUpdateMemberMethodParameter(0);

        // when&then
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("PUT", "/members/my");
        assertThatThrownBy(() ->
                authArgumentResolver.resolveArgument(
                        parameter,
                        null,
                        new ServletWebRequest(servletRequest),
                        null
                )
        ).isInstanceOf(UnauthorizedException.class);
    }

    private MethodParameter createUpdateMemberMethodParameter(int parameterIndex) throws NoSuchMethodException {
        return new MethodParameter(
                memberController.getClass()
                        .getMethod("updateMember", Long.class, UpdateMemberRequest.class), parameterIndex);
    }
}