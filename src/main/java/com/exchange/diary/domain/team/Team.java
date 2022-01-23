package com.exchange.diary.domain.team;

import com.exchange.diary.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Team {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long teamId;

    private String teamLink;

    private String teamName;

    @OneToOne
    @JoinColumn(name = "member_number")
    private Member memberAdmin;

    @OneToMany(mappedBy = "team")
    private List<TeamMember> memberList = new ArrayList<>();

    @Builder
    public Team(String teamName,Member member){
        this.teamName = teamName;
        this.memberAdmin = member;
    }

}
