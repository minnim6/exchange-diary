package com.exchange.diary.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    //TODO N+1 처리
    Member findByMemberId(String memberId);
    Member findByMemberNumber(Long memberNumber);
    boolean existsByMemberIdAndMemberNickname(String memberId,String memberNickname);
}
