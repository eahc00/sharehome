package com.sharehome.place.controller;

import static com.sharehome.fixture.MemberFixture.영채_로그인_request;
import static com.sharehome.fixture.MemberFixture.영채_회원가입_request;
import static com.sharehome.fixture.PlaceFixture.불가능일_설정_request;
import static com.sharehome.fixture.PlaceFixture.숙소_등록_request;
import static com.sharehome.fixture.PlaceFixture.숙소_수정_request;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.sharehome.member.controller.request.LoginRequest;
import com.sharehome.member.controller.request.SignupRequest;
import com.sharehome.place.controller.request.PlaceRegisterRequest;
import com.sharehome.place.controller.request.PlaceSearchRequest;
import com.sharehome.place.controller.request.PlaceUpdateRequest;
import com.sharehome.place.controller.request.UnavailableDateUpdateRequest;
import com.sharehome.place.domain.PlaceRepository;
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
public class PlaceApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    PlaceRepository placeRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        placeRepository.deleteAll();
    }

    @Test
    void 숙소_등록_성공() {
        // given
        joinMemberRequest(영채_회원가입_request());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(영채_로그인_request());
        String sessionId = loginResponse.cookie("JSESSIONID");
        PlaceRegisterRequest request = 숙소_등록_request();

        // when
        ExtractableResponse<Response> response = placeRegisterRequest(sessionId, request);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void 숙소_예약_불가능일_등록_성공() {
        // given
        joinMemberRequest(영채_회원가입_request());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(영채_로그인_request());
        String sessionId = loginResponse.cookie("JSESSIONID");
        ExtractableResponse<Response> placeRegisterResponse = placeRegisterRequest(sessionId, 숙소_등록_request());
        String placeUri = placeRegisterResponse.header("Location");
        UnavailableDateUpdateRequest request = 불가능일_설정_request();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(request)
                .when().post(placeUri + "/unavailable-date")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @Test
    void 숙소_조회_성공() {
        // given
        joinMemberRequest(영채_회원가입_request());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(영채_로그인_request());
        String sessionId = loginResponse.cookie("JSESSIONID");
        ExtractableResponse<Response> placeRegisterResponse = placeRegisterRequest(sessionId, 숙소_등록_request());
        String placeUri = placeRegisterResponse.header("Location");

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(placeUri)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 숙소_검색_성공() {
        // given
        joinMemberRequest(영채_회원가입_request());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(영채_로그인_request());
        String sessionId = loginResponse.cookie("JSESSIONID");
        placeRegisterRequest(sessionId, 숙소_등록_request());

        PlaceSearchRequest request = new PlaceSearchRequest(
                "채리", "대전", 2, null, null
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().get("/places")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 숙소_수정_성공() {
        // given
        joinMemberRequest(영채_회원가입_request());
        ExtractableResponse<Response> loginResponse = loginMemberRequest(영채_로그인_request());
        String sessionId = loginResponse.cookie("JSESSIONID");
        ExtractableResponse<Response> placeRegisterResponse = placeRegisterRequest(sessionId, 숙소_등록_request());
        String placeUri = placeRegisterResponse.header("Location");
        PlaceUpdateRequest request = 숙소_수정_request();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .sessionId(sessionId)
                .body(request)
                .when().put(placeUri)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
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
