package com.exchange.diary.domain.member.dto;

import com.exchange.diary.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @NoArgsConstructor
    @Getter
    public static class RequestUpdate{
        private String memberNickname;
    }

    @NoArgsConstructor
    @Getter
    public static class RequestLogin {
        private String memberId;
        private String memberPassword;
    }

    @NoArgsConstructor
    @Getter
    public static class ResponseInfo {
        private Long memberNumber;
        private String memberId;
        private String memberNickname;
        private String memberPassword;
        private Date memberJoinDate;
    }

    @NoArgsConstructor
    @Getter
    public static class ResponseNickname {
        private Long memberNumber;
        private String memberNickname;
    }
}
