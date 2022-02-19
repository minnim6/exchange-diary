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

        // 밸리데이션 (이 로직을 수행하는데 필수 조건)
        // 로직

        // if가 있으면 else도 있어야 한다.
        // => kotlin let을 쓸때의 주의점과 동일함.


//        if (true) {
//            return ~;
//        } else {
//            return "";
//        }
//
//        return null;

        // extract 메소드 분리를 해주거나 하면 된다. 아니면 공통 유틸로 빼면 되는데
        // 주의할 점은 코드 레발에서의 밸리데이션은 코드레벨에서만 이뤄져야 한다.
        // 예를들어 어떤 객체가 null인지(객체안의 필드까지를 포함함)
        // 이걸 일일히 필드가 널인지 블랭크인지 체크하는 로직 vs 리플렉션으로 객체의 모든 필드를 순회하면서 널인지를 체크하는 로직
        // 리플렉션을 고르는 게 아니라 코드로 일일히 하는 편이 좋다.
        // 일관성 (코드 레벨의 문제는 코드에서)

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

    public void joinTeam(String teamUrl){ //-> url 로 입장 해서 해당 멤버 조인 ~\
        Team team = teamRepository.findByTeamLink(teamUrl);
        saveTeamMember(getMemberEntity(),team);
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

    public void updateTeamName(TeamDto.RequestUpdateTeam requestUpdateTeam){
        Team team = getTeamEntity(requestUpdateTeam.getTeamId());
        team.updateTeamName(requestUpdateTeam.getTeamName());
    }

    public TeamDto.ResponseLink createTeam(TeamDto.RequestCreateTeam requestCreateTeam){
        Member member = getMemberEntity();
        Team team = Team.builder()
                .teamName(requestCreateTeam.teamName)
                .memberAdmin(member)
                .build();
        team.createTeamLink();
        saveTeam(team);
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

    private void saveTeam(Team team){
        teamRepository.save(team);
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
