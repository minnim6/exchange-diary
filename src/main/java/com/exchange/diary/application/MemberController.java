package com.exchange.diary.application;

import com.exchange.diary.domain.member.MemberService;
import com.exchange.diary.domain.member.MemberDto;
import com.exchange.diary.infrastructure.jwt.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public Jwt.Response loginMember(@RequestBody MemberDto.RequestLogin requestLogin){
        return memberService.loginMember(requestLogin);
    }

    @PostMapping("/sign")
    public void signMember(@RequestBody MemberDto.RequestSignup requestSignup){
        memberService.signupMember(requestSignup);
    }

    @DeleteMapping("/delete")
    public void deleteMember(){
        memberService.deleteMember();
    }

    @PatchMapping("/update")
    public MemberDto.ResponseInfo updateMember(MemberDto.RequestUpdate requestUpdate){
        return memberService.updateMember(requestUpdate);
    }

}
