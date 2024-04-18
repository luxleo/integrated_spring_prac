package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceV2V3Test {
    private static final String MemberA="memberA";
    private static final String MemberB="memberB";
    private static final String MemberEX="ex";
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberServiceV3 memberService;

    @AfterEach
    void afterEach(){
        memberRepository.delete(MemberA);
        memberRepository.delete(MemberB);
        memberRepository.delete(MemberEX);
    }

    @Test
    void aopCheck(){
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

    @Test
    void successTX(){
        //given
        int initialMoney = 10_000;
        int amount = 2000;
        Member memberA = new Member(MemberA, initialMoney);
        Member memberB = new Member(MemberB, initialMoney);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(MemberA,MemberB,amount);

        //then
        Member updatedMemberA = memberRepository.findById(MemberA);
        Member updatedMemberB = memberRepository.findById(MemberB);

        Assertions.assertThat(updatedMemberA.getMoney()).isEqualTo(initialMoney-amount);
        Assertions.assertThat(updatedMemberB.getMoney()).isEqualTo(initialMoney+amount);
    }
    @Test
    void failTx(){
        //given
        int initialMoney = 10_000;
        int amount = 2000;
        Member memberA = new Member(MemberA, initialMoney);
        Member memberB = new Member(MemberEX, initialMoney);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        Assertions.assertThatThrownBy(()->memberService.accountTransfer(MemberA,MemberEX,amount))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member updatedMemberA = memberRepository.findById(MemberA);
        Member updatedMemberB = memberRepository.findById(MemberEX);

        Assertions.assertThat(updatedMemberA.getMoney()).isEqualTo(initialMoney);
        Assertions.assertThat(updatedMemberB.getMoney()).isEqualTo(initialMoney);
    }

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig{
        private final DataSource dataSource;
        @Bean
        MemberRepository memberRepository(){
            return new MemberRepositoryV3_1(dataSource);
        }
        @Bean
        MemberServiceV3 memberServiceV3(){
            return new MemberServiceV3(memberRepository());
        }
    }
}