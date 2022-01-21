package com.exchange.diary.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> {
}
