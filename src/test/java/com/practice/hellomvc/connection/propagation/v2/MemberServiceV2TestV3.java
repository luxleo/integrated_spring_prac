package com.practice.hellomvc.connection.propagation.v2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


/**
 * MemberService에서 단일 트랜잭션으로 관리하여 두 레포지토리의 작업을 묶을수 있다.
 * * MemberService @Transactional:ON
 * * MemberRepository @Transactional:OFF
 * * LogRepository @Transactional:OFF
 */
@Slf4j
@SpringBootTest
class MemberServiceV2TestV3 {
    @Autowired
    MemberServiceV2 memberService;
    @Autowired
    MemberRepositoryV2 memberRepositoryV2;
    @Autowired
    LogRepositoryV2 logRepositoryV2;
    
    @Test
    void fail_single_tx(){
        String username = "fail_ex";
        assertThatThrownBy(()->memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        assertThat(memberRepositoryV2.findByName(username)).isEmpty();
        assertThat(logRepositoryV2.findByMessage(username)).isEmpty();
    }
}