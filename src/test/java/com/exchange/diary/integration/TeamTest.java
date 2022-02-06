package com.exchange.diary.integration;

import com.exchange.diary.domain.team.TeamDto;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamTest {

    @LocalServerPort
    int port;

    @Autowired
    JwtUtil jwtUtil;

    String token;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        token = jwtUtil.createAccessToken(1L).getAccessToken();
        RestAssured.port = port;
    }

    @DisplayName("팀 생성 테스트")
    @Test
    public void createTeamTest() throws JsonProcessingException {
        TeamDto.RequestCreateTeam team = new TeamDto.RequestCreateTeam("newTeam");
        given().accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .body(objectMapper.writeValueAsString(team)).log().all()
                .when().post("/team")
                .then().statusCode(HttpStatus.OK.value());
    }
}
