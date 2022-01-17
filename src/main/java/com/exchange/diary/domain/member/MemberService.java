package com.exchange.diary.domain.member;

import com.exchange.diary.domain.member.dto.MemberDto;
import com.exchange.diary.infrastructure.jwt.Jwt;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

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

    public void signupMember(MemberDto.RequestSignup requestSignup) {
        requestSignup.setPassword(passwordEncoder.encode(requestSignup.getMemberPassword()));
        Member member = memberRepository.save(requestSignup.toEntity());
    }

    public void deleteMember(){

    }

    public void updateMember(){

    }

    public void getMember(){

    }

    public boolean isDuplicationCheckMember(MemberDto.RequestSignup requestSignup){
        return !memberRepository.existsByMemberIdAndMemberNickname(requestSignup.getMemberId(), requestSignup.getMemberNickname());
    }


    public boolean isMatchesPassword(String encoderPassword,String password) {
        return passwordEncoder.matches(encoderPassword,password);
    }

}
