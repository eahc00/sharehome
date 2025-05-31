package com.sharehome.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sharehome.member.controller.request.SignupRequest;
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
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MemberApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    protected void setup() {
        RestAssured.port = port;
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest request = new SignupRequest(
                "eahc00@naver.com",
                "영채",
                LocalDate.of(2002, 10, 9),
                "Abc123##"
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 중복된_이메일로_회원가입_시_실패() {
        // given
        SignupRequest request = new SignupRequest(
                "eahc00@naver.com",
                "영채",
                LocalDate.of(2002, 10, 9),
                "Abc123##"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then()
                .log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
