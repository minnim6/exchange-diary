package com.exchange.diary.service;

import com.exchange.diary.domain.diary.DiaryRepository;
import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberDto;
import com.exchange.diary.domain.member.MemberRepository;
import com.exchange.diary.domain.member.MemberService;
import com.exchange.diary.domain.team.Team;
import com.exchange.diary.domain.team.TeamMember;
import com.exchange.diary.domain.team.TeamMemberRepository;
import com.exchange.diary.domain.team.TeamRepository;
import com.exchange.diary.infrastructure.jwt.Jwt;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    MemberRepository memberRepository = mock(MemberRepository.class);
    TeamRepository teamRepository = mock(TeamRepository.class);
    DiaryRepository diaryRepository = mock(DiaryRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    JwtUtil jwtUtil = mock(JwtUtil.class);

    MemberService memberService = new MemberService(memberRepository, teamRepository, diaryRepository, jwtUtil, passwordEncoder);

    Member member;

    MemberDto.RequestSignup signupMember = new MemberDto.RequestSignup();

    @BeforeEach
    public void setup() {
        String memberId = "id";
        String memberPassword = passwordEncoder.encode("password");
        String memberNickname = "nickname";

        member = Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberNickname(memberNickname)
                .build();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        UserDetails userDetails = new User(String.valueOf(1L), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", authorities));
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    public void signMemberTest() {
        //given
        given(memberRepository.existsByMemberId(signupMember.getMemberId())).willReturn(false);
        given(memberRepository.save(any())).willReturn(member);
        given(memberRepository.findByMemberNumber(1L)).willReturn(member);
        //when
        memberService.signupMember(signupMember);
        //then
        assertThat(memberRepository.findByMemberNumber(1L)).isNotNull();
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    public void loginTest() {
        //given
        given(memberRepository.save(any())).willReturn(member);
        given(memberRepository.findByMemberId("id")).willReturn(member);
        given(passwordEncoder.matches("password", member.getMemberPassword())).willReturn(true);
        given(jwtUtil.createJwt(any())).willReturn(Jwt.Response.builder().accessToken("access").refreshToken("refresh").accessTokenExpireTime(new Date()).build());
        MemberDto.RequestLogin requestLogin = new MemberDto.RequestLogin("id", "password");
        //when
        Jwt.Response response = memberService.loginMember(requestLogin);
        //then
        assertThat(response).isNotNull();
    }

    @DisplayName("나의 모든 팀 가져오기 테스트")
    @Test
    public void findAllMyTeam() {

    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    public void deleteMemberTest() {
        //given
        given(memberRepository.save(any())).willReturn(member);
        //when
        memberService.deleteMember();
        //then
        assertThat(memberRepository.findByMemberNumber(1L)).isNull();
    }

    @DisplayName("회원 정보 업데이트 테스트")
    @Test
    public void updateMemberTest() {
        //given
        String changeMemberNickname = "updateNickname";
        given(memberRepository.findByMemberNumber(1L)).willReturn(member);
        MemberDto.RequestUpdate requestUpdate = new MemberDto.RequestUpdate(changeMemberNickname);
        //when
        MemberDto.ResponseInfo responseInfo = memberService.updateMember(requestUpdate);
        //then
        assertThat(responseInfo.getMemberNickname()).isEqualTo(changeMemberNickname);
    }
}
