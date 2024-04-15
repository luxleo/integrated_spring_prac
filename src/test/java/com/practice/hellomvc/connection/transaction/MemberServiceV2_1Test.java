package com.practice.hellomvc.connection.transaction;

import com.practice.hellomvc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static com.practice.hellomvc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberServiceV2_1Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";
    private MemberServiceV2_1 memberService;
    private MemberRepositoryV2 memberRepository;

    @BeforeEach
    void beforeEach(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);

        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV2_1(txManager,memberRepository);
    }

    @AfterEach
    void afterEach() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("트랜잭션 성공")
    void successTx() throws SQLException {
        // given
        int amount = 2000;
        int initialMoney = 10000;

        Member memberA = new Member(MEMBER_A, initialMoney);
        Member memberB = new Member(MEMBER_B, initialMoney);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(MEMBER_A, MEMBER_B,amount);

        //then
        Member findMemberA = memberRepository.findById(MEMBER_A);
        Member findMemberB = memberRepository.findById(MEMBER_B);

        assertThat(findMemberA.getMoney()).isEqualTo(initialMoney-amount);
        assertThat(findMemberB.getMoney()).isEqualTo(initialMoney+amount);

    }

    @Test
    @DisplayName("트랜잭션 실패")
    void failTx() throws SQLException {
        // given
        int amount = 2000;
        int initialMoney = 10000;

        Member memberA = new Member(MEMBER_A, initialMoney);
        Member memberEX = new Member(MEMBER_EX, initialMoney);

        memberRepository.save(memberA);
        memberRepository.save(memberEX);

        //when
        assertThatThrownBy(()->memberService.accountTransfer(MEMBER_A, MEMBER_EX,amount))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member findMemberA = memberRepository.findById(MEMBER_A);
        Member findMemberEX = memberRepository.findById(MEMBER_EX);

        assertThat(findMemberA.getMoney()).isEqualTo(initialMoney);
        assertThat(findMemberEX.getMoney()).isEqualTo(initialMoney);

    }
}