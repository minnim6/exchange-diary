package com.exchange.diary.application;

import com.exchange.diary.domain.member.MemberService;
import com.exchange.diary.domain.member.MemberDto;
import com.exchange.diary.infrastructure.jwt.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public Jwt.Response loginMember(@RequestBody MemberDto.RequestLogin requestLogin){
        return memberService.loginMember(requestLogin);
    }

    @PostMapping("/sign")
    public void signMember(@RequestBody MemberDto.RequestSignup requestSignup){
        memberService.signupMember(requestSignup);
    }

    @DeleteMapping
    public void deleteMember(){
        memberService.deleteMember();
    }

    @PatchMapping
    public MemberDto.ResponseInfo updateMember(MemberDto.RequestUpdate requestUpdate){
        return memberService.updateMember(requestUpdate);
    }

    @GetMapping("/team")
    public List<MemberDto.ResponseMyTeam> getMyTeam(){
        return memberService.findByAllMyTeam();
    }

    @GetMapping("/profile")
    public MemberDto.ResponseInfo getMember(){
       return memberService.getMember();
    }

}
