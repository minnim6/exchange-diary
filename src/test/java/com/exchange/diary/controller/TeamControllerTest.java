package com.exchange.diary.controller;

import com.exchange.diary.application.MemberController;
import com.exchange.diary.application.TeamController;
import com.exchange.diary.domain.team.TeamDto;
import com.exchange.diary.domain.team.TeamService;
import com.exchange.diary.infrastructure.jwt.Jwt;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(value = TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private TeamService teamService;

    @DisplayName("팀 생성")
    @Test
    public void createTeamTest() throws Exception {

        TeamDto.ResponseLink responseLink = new TeamDto.ResponseLink("랜덤생성된 링크");

        doReturn(responseLink).when(teamService).createTeam(any());

        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("teamName","생성할 팀이름 지정");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/team")
                        .header("Authorization", "accessToken")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team/create",
                        preprocessRequest( modifyUris()
                                .scheme("http")
                                .host("jeom.shop")
                                .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("생성할 팀의 이름")
                        ),
                        responseFields(
                                fieldWithPath("teamLink").type(JsonFieldType.STRING).description("생성 완료된 팀의 고유 링크")
                        )
                ));

    }

    @DisplayName("팀 삭제")
    @Test
    public void deleteTeam()throws Exception{
        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("teamId",1);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/team")
                        .header("Authorization", "accessToken")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team/delete",
                        preprocessRequest( modifyUris()
                                .scheme("http")
                                .host("jeom.shop")
                                .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("삭제할 팀의 고유 id")
                        )
                ));
    }

    @DisplayName("팀 메인페이지가져오기")
    @Test
    public void getTeamInfo() throws Exception {

        List<String> todayMemberList = new ArrayList<>();
        todayMemberList.add("오늘 글쓴 회원의 닉네임");

        TeamDto.ResponseInfo responseInfo = new TeamDto.ResponseInfo(2,1,todayMemberList,
                TeamDto.ResponseInfo.CheckWroteMy.Y);

        doReturn(responseInfo).when(teamService).getTeamInfo(any());

        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("teamId",1);
        requestJson.put("teamDate", "2020-04-30");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/team")
                        .header("Authorization", "accessToken")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team/info",
                        preprocessRequest( modifyUris()
                                .scheme("http")
                                .host("jeom.shop")
                                .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀의 고유 id"),
                                fieldWithPath("teamDate").type(JsonFieldType.STRING).description("가져올 날짜")
                        ),
                        responseFields(
                                fieldWithPath("members").type(JsonFieldType.NUMBER).description("총 회원의 수"),
                                fieldWithPath("wroteMembers").type(JsonFieldType.NUMBER).description("오늘 일기를 작성한 회원의 수"),
                                fieldWithPath("todayMemberList").description("오늘 일기를 작성한 팀원의 닉네임"),
                                fieldWithPath("checkWroteMy").type(JsonFieldType.STRING).description("해당 날짜에 내가 글을 작성했는지 여부")
                        )
                ));

    }


    @DisplayName("팀 가입하기")
    @Test
    public void joinTeam() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.post("/team/고유링크")
                        .header("Authorization", "accessToken"))
                .andExpect(status().isOk())
                .andDo(document("team/join",
                        preprocessRequest( modifyUris()
                                .scheme("http")
                                .host("jeom.shop")
                                .removePort(),prettyPrint())
                ));

    }


    @DisplayName("팀이름 바꾸기")
    @Test
    public void updateTeam() throws Exception {

        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("teamId",1);
        requestJson.put("teamName", "변경할 팀네임");

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/team")
                        .header("Authorization", "accessToken")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("team/update",
                        preprocessRequest( modifyUris()
                                .scheme("http")
                                .host("jeom.shop")
                                .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀의 고유 id"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("변경할 팀닉네임")
                        )
                ));

    }
}
