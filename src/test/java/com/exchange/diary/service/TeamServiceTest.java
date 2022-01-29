package com.exchange.diary.service;
import static org.mockito.Mockito.*;

import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberRepository;
import com.exchange.diary.domain.team.*;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class TeamServiceTest {

    TeamRepository teamRepository = mock(TeamRepository.class);
    MemberRepository memberRepository = mock(MemberRepository.class);
    TeamMemberRepository teamMemberRepository = mock(TeamMemberRepository.class);
    JwtUtil jwtUtil = mock(JwtUtil.class);

    TeamService teamService = new TeamService(teamRepository,memberRepository,teamMemberRepository);

    Member member;

    Team team;

    @BeforeEach
    public void setup(){

        String memberId = "id";
        String memberPassword = "password";
        String memberNickname = "nickname";

        member = Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberNickname(memberNickname)
                .build();

        team = Team.builder()
                .teamName("newTeam")
                .memberAdmin(member)
                .build();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        UserDetails userDetails = new User(String.valueOf(1L), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", authorities));
    }

    @DisplayName("팀 생성 테스트")
    @Test
    public void createTeam(){
        //given
        TeamDto.RequestCreateTeam requestCreateTeam = new TeamDto.RequestCreateTeam("newTeam");
        TeamMember teamMember = new TeamMember(team,member);
        given(memberRepository.findByMemberNumber(1L)).willReturn(member);
        given(teamRepository.save(team)).willReturn(team);
        given(teamMemberRepository.save(teamMember)).willReturn(teamMember);
        //when
        //then
    }
}
