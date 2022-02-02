package com.exchange.diary.domain.team;


import com.exchange.diary.domain.diary.DiaryRepository;
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

    private final DiaryRepository diaryRepository;

    public void joinTeam(TeamDto.RequestTeamId requestTeamId){
        Member member = getMemberEntity();
        saveTeamMember(member,getTeamEntity(requestTeamId.teamId));
    }

    public TeamDto.ResponseJoin getJoinInfo(String teamLink){
        return new TeamDto.ResponseJoin(teamRepository.findByTeamLink(teamLink));
    }

    public TeamDto.ResponseCreate createTeam(TeamDto.RequestCreateTeam requestCreateTeam){
        Member member = getMemberEntity();
        Team team = teamRepository.save(Team.builder()
                .teamName(requestCreateTeam.teamName)
                .memberAdmin(member)
                .build());
        saveTeamMember(member,team);
        return new TeamDto.ResponseCreate(team);
    }

    public void deleteTeam(TeamDto.RequestTeamId requestTeamId){
        Team team = getTeamEntity(requestTeamId.getTeamId());
        if(isCheckTeamAdmin(team.getMemberAdmin().getMemberNumber())){
            diaryRepository.deleteAllByTeam(team);
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
