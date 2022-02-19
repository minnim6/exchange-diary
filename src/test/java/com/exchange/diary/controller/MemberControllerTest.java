package com.exchange.diary.controller;

import com.exchange.diary.application.MemberController;
import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberDto;
import com.exchange.diary.domain.member.MemberRepository;
import com.exchange.diary.domain.member.MemberService;
import com.exchange.diary.infrastructure.jwt.Jwt;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.RequestEntity.post;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.mockito.Mockito.doReturn;
import static org.mockito.BDDMockito.given;

@AutoConfigureRestDocs
@WebMvcTest(value = MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void loginMemberTest() throws Exception {

        Jwt.Response response = Jwt.Response.builder()
                .accessTokenExpireTime(new Date())
                .refreshToken("refreshToken")
                .accessToken("accessToken")
                .build();

        doReturn(response).when(memberService).loginMember(any());

        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("memberId", "id");
        requestJson.put("memberPassword", "1111");

        mockMvc.perform(RestDocumentationRequestBuilders.post("/member/login")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(document("member/login",
                                preprocessRequest( modifyUris()
                                        .scheme("http")
                                        .host("jeom.shop")
                                        .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberId").type(JsonFieldType.STRING).description("회원 id"),
                                fieldWithPath("memberPassword").type(JsonFieldType.STRING).description("회원 password")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                                fieldWithPath("accessTokenExpireTime").description("만료 날짜")
                        )
                ));
    }

    @Test
    public void signMemberTest() throws Exception {

        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("memberId", "id");
        requestJson.put("memberPassword", "pass");
        requestJson.put("memberNickname", "닉네임");


        mockMvc.perform(RestDocumentationRequestBuilders.post("/member/sign")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(document("member/sign",
                                preprocessRequest( modifyUris()
                                        .scheme("http")
                                        .host("jeom.shop")
                                        .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("memberId").type(JsonFieldType.STRING).description("회원 id"),
                                fieldWithPath("memberPassword").type(JsonFieldType.STRING).description("회원 password")
                        )
                ));
    }

    @Test
    public void updateMemberTest() throws Exception {

        MemberDto.ResponseInfo responseInfo = new MemberDto.ResponseInfo("id", "pass", LocalDate.now());

        doReturn(responseInfo).when(memberService).updateMember(any());

        Map<String, Object> requestJson = new HashMap<>();

        requestJson.put("memberNickname", "변경");


        mockMvc.perform(RestDocumentationRequestBuilders.patch("/member")
                        .header("Authorization", "accessToken")
                        .content(objectMapper.writeValueAsString(requestJson))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(document("member/update",
                                preprocessRequest( modifyUris()
                                        .scheme("http")
                                        .host("jeom.shop")
                                        .removePort(),prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("변경할 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("바뀐닉네임"),
                                fieldWithPath("memberJoinDate").description("가입 날짜")
                        )
                ));
    }

    @Test
    public void deleteMemberTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/member")
                        .header("Authorization", "accessToken"))
                        .andExpect(status().isOk())
                        .andDo(document("member/delete",
                                preprocessRequest( modifyUris()
                                        .scheme("http")
                                        .host("jeom.shop")
                                        .removePort(),prettyPrint())
                                ));
    }

    @Test
    public void getMember() throws Exception {

        MemberDto.ResponseInfo responseInfo = new MemberDto.ResponseInfo("id", "pass", LocalDate.now());

        doReturn(responseInfo).when(memberService).getMember();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/member/profile")
                        .header("Authorization","accessToken"))
                        .andExpect(status().isOk())
                        .andDo(document("member/profile",
                        preprocessRequest(
                                modifyUris()
                                        .scheme("http")
                                                .host("jeom.shop")
                                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("바뀐닉네임"),
                                fieldWithPath("memberJoinDate").description("가입 날짜")
                        )
                ));
    }

}
