package com.exchange.diary.domain.team;


import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final MemberRepository memberRepository;

    private final TeamMemberRepository teamMemberRepository;

    public void joinTeam(){

    }

    public Long createTeam(TeamDto.RequestCreateTeam requestCreateTeam){
        Member member = getMember();
        Team team = teamRepository.save(Team.builder()
                .teamName(requestCreateTeam.teamName)
                .memberAdmin(member)
                .build());
        saveTeamMember(member,team);
        return team.getTeamId();
    }

    public void deleteTeam(){

    }

    private void saveTeamMember(Member member,Team team){
        teamMemberRepository.save(TeamMember.builder()
                .member(member)
                .team(team)
                .build());
    }

    private Long getMemberNumber(){
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private Member getMember(){
        return memberRepository.findByMemberNumber(getMemberNumber());
    }

}
