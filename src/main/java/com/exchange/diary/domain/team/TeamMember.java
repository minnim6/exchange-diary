package com.exchange.diary.domain.team;

import com.exchange.diary.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TeamMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long teamMemberId;

    @JoinColumn(name = "team_id")
    @ManyToOne
    private Team team;

    @JoinColumn(name = "member_number")
    @ManyToOne
    private Member member;

    @Builder
    public TeamMember(Team team,Member member){
        this.member = member;
        this.team = team;
    }
}
