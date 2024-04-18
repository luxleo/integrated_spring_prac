package com.practice.hellomvc.connection.propagation.v3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 *  * * MemberService @Transactional:ON
 *  * * MemberRepository @Transactional:ON
 *  * * LogRepository @Transactional:ON
 */
@Slf4j
@SpringBootTest
class MemberServiceV4Test {
    @Autowired
    MemberServiceV3 memberService;
    @Autowired
    MemberRepositoryV3 memberRepository;
    @Autowired
    LogRepositoryV3 logRepository;

    @Test
    void fail_tx(){
        String username = "fail_ex";
        assertThatThrownBy(()->memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        assertThat(memberRepository.findByName(username)).isEmpty();
        assertThat(logRepository.findByMessage(username)).isEmpty();
    }
}