package com.exchange.diary.domain.member;

import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.team.Team;
import com.exchange.diary.domain.team.TeamMember;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
public class MemberDto {

    @NoArgsConstructor
    @Getter
    public static class RequestSignup {
        private String memberId;
        private String memberNickname;
        private String memberPassword;

        public Member toEntity() {
           return Member.builder()
                    .memberId(this.memberId)
                    .memberNickname(this.memberNickname)
                    .memberPassword(this.memberPassword)
                    .build();
        }

        public void setPassword(String memberPassword){
            this.memberPassword = memberPassword;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RequestUpdate{
        private String memberNickname;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RequestLogin {
        private String memberId;
        private String memberPassword;
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseMyTeam{
        private Long teamId;
        private String teamName;
        private int teamTotalMemberNumber;
        private int wroteTodayMemberNumber;

        public ResponseMyTeam(TeamMember teamMember, Team team, int wroteTodayMemberNumber){
            this.teamId = teamMember.getTeam().getTeamId();
            this.teamName = teamMember.getTeam().getTeamName();
            this.teamTotalMemberNumber = team.getMemberList().size();
            this.wroteTodayMemberNumber = wroteTodayMemberNumber;
        }

        public void setTeamTotalMemberNumber(int totalMemberNumber){
            this.teamTotalMemberNumber = totalMemberNumber;
        }

        public void setWroteTodayMemberNumber(int wroteTodayMemberNumber){
            this.wroteTodayMemberNumber = wroteTodayMemberNumber;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseInfo {
        private String memberId;
        private String memberNickname;
        private LocalDate memberJoinDate;

        public ResponseInfo(Member member){
           this.memberId = member.getMemberId();
           this.memberNickname = member.getMemberNickname();
           this.memberJoinDate = member.getMemberJoinDate();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class ResponseNickname {
        private Long memberNumber;
        private String memberNickname;
    }
}
