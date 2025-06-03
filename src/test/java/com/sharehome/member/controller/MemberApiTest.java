package com.sharehome.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.sharehome.member.controller.request.ChangePasswordRequest;
import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.member.controller.request.UpdateMemberRequest;
import com.sharehome.member.domain.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MemberApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest request = createSignupRequest();

        // when
        ExtractableResponse<Response> response = joinMemberRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void 중복된_이메일로_회원가입_시_실패() {
        // given
        SignupRequest request = createSignupRequest();
        joinMemberRequest(request);

        // when
        ExtractableResponse<Response> response = joinMemberRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    @Test
    void 로그인_성공() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);

        LoginRequest request = createLoginRequest(signupRequest.email(), signupRequest.password());

        // when
        ExtractableResponse<Response> response = loginMemberRequest(request);

        // then
        assertThat(response.cookie("JSESSIONID")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 이메일이_없으면_로그인_실패() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);

        LoginRequest request = createLoginRequest("invalid@domain.com", signupRequest.password());

        // when
        ExtractableResponse<Response> response = loginMemberRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
        assertThat(response.cookie("JSESSIONID")).isNull();
    }

    @Test
    void 비밀번호가_틀리면_로그인_실패() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);

        LoginRequest request = createLoginRequest(signupRequest.email(), "Invalid1234@");

        // when
        ExtractableResponse<Response> response = loginMemberRequest(request);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
        assertThat(response.cookie("JSESSIONID")).isNull();
    }

    @Test
    void 로그인_이후에만_계정관리를_할_수_있다() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);
        LoginRequest loginRequest = createLoginRequest(signupRequest.email(), signupRequest.password());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(loginRequest);
        String sessionId = loginResponse.cookie("JSESSIONID");

        UpdateMemberRequest request = createUpdateMemberRequest();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/members/my")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인_하지_않고_계정관리_시_예외() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);

        UpdateMemberRequest request = createUpdateMemberRequest();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .cookie("JSESSIONID", null)
                .contentType(ContentType.JSON)
                .body(request)
                .when().put("/members/my")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 비밀번호_변경_성공() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);
        LoginRequest loginRequest = createLoginRequest(signupRequest.email(), signupRequest.password());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(loginRequest);
        String sessionId = loginResponse.cookie("JSESSIONID");

        ChangePasswordRequest request = new ChangePasswordRequest(
                signupRequest.password(), "newPassword1234@"
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members/change_password")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 비밀번호_변경_실패() {
        // given
        SignupRequest signupRequest = createSignupRequest();
        joinMemberRequest(signupRequest);
        LoginRequest loginRequest = createLoginRequest(signupRequest.email(), signupRequest.password());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(loginRequest);
        String sessionId = loginResponse.cookie("JSESSIONID");

        ChangePasswordRequest request = new ChangePasswordRequest(
                "WrongPassword1234@", "newPassword1234@"
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members/change_password")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    private static SignupRequest createSignupRequest() {
        return new SignupRequest(
                "email@domain.com",
                "하영채",
                LocalDate.of(2002, 10, 9),
                "Password1234@"
        );
    }

    private static LoginRequest createLoginRequest(String email, String password) {
        return new LoginRequest(email, password);
    }

    private static UpdateMemberRequest createUpdateMemberRequest() {
        return new UpdateMemberRequest("채리채리", "대전", "대학로", "12345");
    }

    private static ExtractableResponse<Response> joinMemberRequest(SignupRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then()
                .log().all()
                .extract();
    }

    private static ExtractableResponse<Response> loginMemberRequest(LoginRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members/login")
                .then()
                .log().all()
                .extract();
    }
}
