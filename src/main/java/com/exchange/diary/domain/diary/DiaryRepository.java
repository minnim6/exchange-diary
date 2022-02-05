package com.exchange.diary.domain.diary;

import com.exchange.diary.domain.member.Member;
import com.exchange.diary.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {
    int countAllByTeamAndDiaryDate(Team team, LocalDate date);
    void deleteAllByTeam(Team team);
    boolean existsByMemberAndDiaryDate(Member member, LocalDate date);
}
