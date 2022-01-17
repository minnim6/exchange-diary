package com.exchange.diary.repository;


import com.exchange.diary.domain.team.TeamMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNumber;

    private String memberId;

    private String memberPassword;

    private String memberNickname;

    @Temporal(value = TemporalType.DATE)
    @Column(insertable = true, updatable = false)
    @CreationTimestamp
    private Date memberJoinDate;

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
