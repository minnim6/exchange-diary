package com.exchange.diary.domain.member;

import com.exchange.diary.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;


    public void loginMember(){

    }

    public void signupMember(MemberDto.RequestSignup requestSignup){
        //TODO password encoder
        memberRepository.save(requestSignup.toEntity());
    }

    public void deleteMember(Long memberNumber){
        memberRepository.deleteById(memberNumber);
    }

    public void updateMember(){

    }

    public void getMember(){

    }

}
