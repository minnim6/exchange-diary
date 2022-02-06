package com.exchange.diary.domain.team;


import com.exchange.diary.domain.diary.DiaryRepository;
import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final MemberRepository memberRepository;

    private final TeamMemberRepository teamMemberRepository;

    private final DiaryRepository diaryRepository;

    public TeamDto.ResponseInfo getTeamInfo(TeamDto.RequestTeam requestTeam){
        if(requestTeam.teamDate == null){
            requestTeam.setTeamDate();
        }
        Team team = teamRepository.findByTeamId(requestTeam.getTeamId());
        TeamDto.ResponseInfo responseInfo = new TeamDto.ResponseInfo(team);

        responseInfo.setTodayMemberList(todayMember(team.getMemberList()));

        if(isCheckTodayWritten(requestTeam.teamDate)){
            //TODO 이미지 url 추가 해야함
            responseInfo.changeCheckWroteMy();
            return responseInfo;
        }
        return responseInfo;
    }

    private List<String> todayMember(List<TeamMember> memberList){
        List<String> todayMember = new ArrayList<>();
        for(TeamMember member:memberList){
            if(diaryRepository.existsByMemberAndDiaryDate(member.getMember(),LocalDate.now())){
                todayMember.add(member.getMember().getMemberNickname());
            }
        }
        return todayMember;
    }

    private boolean isCheckTodayWritten(LocalDate date){
        return diaryRepository.existsByMemberAndDiaryDate(getMemberEntity(),date);
    }

    public TeamDto.ResponseLink createTeam(TeamDto.RequestCreateTeam requestCreateTeam){
        Member member = getMemberEntity();
        Team team = Team.builder()
                .teamName(requestCreateTeam.teamName)
                .memberAdmin(member)
                .build();
        team.createTeamLink();
        teamRepository.save(team);
        saveTeamMember(member,team);
        return new TeamDto.ResponseLink(team);
    }

    public void deleteTeam(TeamDto.RequestTeamId requestTeamId){
        Team team = getTeamEntity(requestTeamId.getTeamId());
        if(isCheckTeamAdmin(team.getMemberAdmin().getMemberNumber())){
            if(diaryRepository.existsByTeam(team)) {
                diaryRepository.deleteAllByTeam(team);
            }
            teamMemberRepository.deleteAllByTeam(team);
            teamRepository.delete(team);
        }
    }


    private void saveTeamMember(Member member,Team team){
        teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
    }

    public Team getTeamEntity(Long teamId){
        //TODO 에러처리
        return teamRepository.findById(teamId).orElseThrow(NullPointerException::new);
    }

    private boolean isCheckTeamAdmin(Long memberNumber){
        return memberNumber.equals(getMemberNumber());
    }

    private Long getMemberNumber(){
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private Member getMemberEntity(){
        return memberRepository.findByMemberNumber(getMemberNumber());
    }

}
