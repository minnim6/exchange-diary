package com.exchange.diary.domain.member;


import com.exchange.diary.domain.team.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNumber;

    private String memberId;

    private String memberPassword;

    private String memberNickname;

    @Column(insertable = true, updatable = false)
    @CreationTimestamp
    private LocalDate memberJoinDate;

    @OneToMany(mappedBy = "member")
    private List<TeamMember> teamList = new ArrayList<>();

    @Builder
    public Member(String memberId,String memberNickname,String memberPassword){
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.memberPassword = memberPassword;
    }

    public void memberUpdate(String memberNickname){
        this.memberNickname = memberNickname;
    }
}
