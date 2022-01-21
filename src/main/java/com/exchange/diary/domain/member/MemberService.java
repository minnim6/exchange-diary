package com.exchange.diary.domain.member;

import com.exchange.diary.domain.diary.DiaryRepository;
import com.exchange.diary.domain.team.Team;
import com.exchange.diary.domain.team.TeamMember;
import com.exchange.diary.domain.team.TeamRepository;
import com.exchange.diary.infrastructure.jwt.Jwt;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final TeamRepository teamRepository;

    private final DiaryRepository diaryRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    public Jwt.Response loginMember(MemberDto.RequestLogin requestLogin) {
        //TODO null 처리
        Member member = memberRepository.findByMemberId(requestLogin.getMemberId());
        if(isMatchesPassword(member.getMemberPassword(),requestLogin.getMemberPassword())){
            return jwtUtil.createJwt(member.getMemberNumber());
        }
        throw new NullPointerException();
    }

    public List<MemberDto.ResponseMyTeam> findByAllMyTeam(){
        return memberRepository.findByMemberNumber(getMemberNumber())
                .getTeamList()
                .stream()
                .map(entity -> {
                    Team team = getMyTeam(entity.getTeam().getTeamId());
                    return new MemberDto.ResponseMyTeam(entity, team, findWroteTodayMemberNumber(team));
                })
                .collect(Collectors.toList());
    }

    public Team getMyTeam(Long teamId){
        return teamRepository.findById(teamId).orElseThrow(NullPointerException::new); //TODO 커스텀 에러처리
    }

    public int findWroteTodayMemberNumber(Team team){
        return diaryRepository.countAllByTeamAndDiaryDate(team,new Date());
    }

    public void signupMember(MemberDto.RequestSignup requestSignup) {
        requestSignup.setPassword(passwordEncoder.encode(requestSignup.getMemberPassword()));
        Member member = memberRepository.save(requestSignup.toEntity());
    }

    public void deleteMember(){
        memberRepository.deleteById(getMemberNumber());
    }

    public MemberDto.ResponseInfo updateMember(MemberDto.RequestUpdate requestUpdate){
        Member member = memberRepository.findByMemberNumber(getMemberNumber());
        member.memberUpdate(requestUpdate.getMemberNickname());
        return new MemberDto.ResponseInfo(member);
    }

    public MemberDto.ResponseInfo getMember(){
        Member member = memberRepository.findByMemberNumber(getMemberNumber());
        return new MemberDto.ResponseInfo(member);
    }

    private boolean isDuplicationCheckMember(MemberDto.RequestSignup requestSignup){
        return !memberRepository.existsByMemberIdAndMemberNickname(requestSignup.getMemberId(), requestSignup.getMemberNickname());
    }

    private Long getMemberNumber(){
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isMatchesPassword(String encoderPassword,String password) {
        return passwordEncoder.matches(encoderPassword,password);
    }

}
