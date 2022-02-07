package com.exchange.diary.service;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.*;

import com.exchange.diary.domain.diary.Diary;
import com.exchange.diary.domain.diary.DiaryRepository;
import com.exchange.diary.domain.image.BackgroundImage;
import com.exchange.diary.domain.image.Image;
import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberRepository;
import com.exchange.diary.domain.sticker.Sticker;
import com.exchange.diary.domain.team.*;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class TeamServiceTest {

    TeamRepository teamRepository = mock(TeamRepository.class);
    MemberRepository memberRepository = mock(MemberRepository.class);
    TeamMemberRepository teamMemberRepository = mock(TeamMemberRepository.class);
    DiaryRepository diaryRepository = mock(DiaryRepository.class);
    JwtUtil jwtUtil = mock(JwtUtil.class);

    TeamService teamService = new TeamService(teamRepository,memberRepository,teamMemberRepository,diaryRepository);

    Member member;

    Team team;

    LocalDate date = LocalDate.now();

    @BeforeEach
    public void setup(){

        String memberId = "id";
        String memberPassword = "password";
        String memberNickname = "nickname";

        List<TeamMember> teamList = new ArrayList<>();

        member = new Member(1L, "test", "pass", "nick", date,teamList);

        List<TeamMember> memberList = new ArrayList<>();
        memberList.add(new TeamMember(team,member));
        team = new Team(1L,"testLink","teamName",member,memberList);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        UserDetails userDetails = new User(String.valueOf(1L), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", authorities));
    }

    @DisplayName("팀 생성 테스트")
    @Test
    public void createTeamTest(){
        //given
        TeamMember teamMember = new TeamMember(team,member);
        given(memberRepository.findByMemberNumber(1L)).willReturn(member);
        given(teamRepository.save(any())).willReturn(team);
        given(teamMemberRepository.save(teamMember)).willReturn(teamMember);
        //when
        TeamDto.ResponseLink responseLink = teamService.createTeam(new TeamDto.RequestCreateTeam());
        //then
        assertThat(responseLink.getTeamLink()).isNotNull();
    }

    @DisplayName("팀 삭제 테스트")
    @Test
    public void deleteTeamTest(){
        //given
        TeamDto.RequestTeamId requestTeamId = new TeamDto.RequestTeamId(1L);
        Optional<Team> optionalTeam = Optional.ofNullable(team);
        given(teamRepository.findById(1L)).willReturn(optionalTeam);
        given(diaryRepository.existsByTeam(team)).willReturn(true);
        //when
        teamService.deleteTeam(new TeamDto.RequestTeamId());
        //then
        verify(diaryRepository).deleteAllByTeam(team);
        verify(teamMemberRepository).deleteAllByTeam(team);
        verify(teamRepository).delete(team);
    }

    @DisplayName("팀 메인 가져오기 테스트")
    @Test
    public void getTeamInfoTest(){

        List<Sticker> stickerList = new ArrayList<>();
        Diary diary = new Diary(1L,team,member,new BackgroundImage(),new Image(),"content"
        ,date,stickerList);
        TeamDto.RequestTeam requestTeam = new TeamDto.RequestTeam(1L,date);
        given(teamRepository.findByTeamId(1L)).willReturn(team);
        given(memberRepository.findByMemberNumber(1L)).willReturn(member);
        given(diaryRepository.existsByMemberAndDiaryDate(member,date)).willReturn(true);
        given(diaryRepository.countAllByTeamAndDiaryDate(team,date)).willReturn(1);
        TeamDto.ResponseInfo responseInfo = teamService.getTeamInfo(requestTeam);

        assertThat(responseInfo.getTodayMemberList().size()).isEqualTo(1);
        assertThat(responseInfo.getCheckWroteMy()).isEqualTo(TeamDto.ResponseInfo.CheckWroteMy.Y);
    }
}
