package com.sharehome.reservation.controller;

import static com.sharehome.fixture.MemberFixture.영채_로그인_request;
import static com.sharehome.fixture.MemberFixture.영채_회원가입_request;
import static com.sharehome.fixture.PlaceFixture.숙소_등록_request;
import static com.sharehome.fixture.ReservationFixture.예약_request;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;

import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.reservation.controller.request.ReservePlaceRequest;
import com.sharehome.reservation.domain.ReservationRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReservationApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        reservationRepository.deleteAll();
    }

    @Test
    void 예약_성공() {
        // given
        joinMemberRequest(영채_회원가입_request());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(영채_로그인_request());
        String sessionId = loginResponse.cookie("JSESSIONID");
        ExtractableResponse<Response> placeRegisterResponse = placeRegisterRequest(sessionId, 숙소_등록_request());
        Long placeId = Long.parseLong(placeRegisterResponse.header("Location").split("/")[2]);
        ReservePlaceRequest request = 예약_request(placeId);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(request)
                .when().post("/reservation")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
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

    private static ExtractableResponse<Response> placeRegisterRequest(String sessionId, PlaceRegisterRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(request)
                .when().post("/places")
                .then()
                .log().all()
                .extract();
    }
}
