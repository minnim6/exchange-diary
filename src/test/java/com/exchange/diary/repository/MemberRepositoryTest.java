package com.exchange.diary.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
     MemberRepository memberRepository;


    @DisplayName("생성 성공 테스트")
    @Test
    public void createMemberTest(){

        //given
        String id = "testId";
        String nickname = "testNickname";
        String password = "testPassword";

        Member saveMember = Member.builder()
                .memberId(id)
                .memberNickname(nickname)
                .memberPassword(password)
                .build();
        //when
        Member member = memberRepository.save(saveMember);
        //then
        assertThat(member.getMemberId()).isEqualTo(saveMember.getMemberId());
    }

    @DisplayName("업데이트 테스트")
    @Test
    public void updateMemberTest(){

    }


}
