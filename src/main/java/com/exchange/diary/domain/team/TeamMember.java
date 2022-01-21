package com.exchange.diary.domain.team;

import com.exchange.diary.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TeamMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long teamListId;

    @JoinColumn(name = "team_id")
    @ManyToOne
    private Team team;

    @JoinColumn(name = "member_number")
    @ManyToOne
    private Member member;
}
