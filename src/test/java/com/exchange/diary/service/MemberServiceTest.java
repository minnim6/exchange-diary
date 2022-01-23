package com.exchange.diary.service;

import com.exchange.diary.domain.diary.DiaryRepository;
import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.member.MemberDto;
import com.exchange.diary.domain.member.MemberRepository;
import com.exchange.diary.domain.member.MemberService;
import com.exchange.diary.domain.team.TeamRepository;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MemberServiceTest {

    MemberRepository memberRepository = mock(MemberRepository.class);
    TeamRepository teamRepository = mock(TeamRepository.class);
    DiaryRepository diaryRepository = mock(DiaryRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    JwtUtil jwtUtil = mock(JwtUtil.class);

    MemberService memberService = new MemberService(memberRepository,teamRepository,diaryRepository,jwtUtil,passwordEncoder);

    @DisplayName("회원가입 성공 테스트")
    @Test
    public void signMemberTest(){

        String memberId = "id";
        String memberPassword = passwordEncoder.encode("password");
        String memberNickname = "nickname";

        Member member = Member.builder()
                .memberId(memberId)
                .memberPassword(memberPassword)
                .memberNickname(memberNickname)
                .build();
        //given id,nickname,password
        MemberDto.RequestSignup signupMember = new MemberDto.RequestSignup();

        given(memberRepository.existsByMemberId(signupMember.getMemberId())).willReturn(false);
        given(memberRepository.save(any())).willReturn(member);
        //when
        memberService.signupMember(signupMember);
        //then
    }

}
