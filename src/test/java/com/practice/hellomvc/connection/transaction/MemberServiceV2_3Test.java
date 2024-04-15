package com.practice.hellomvc.connection.transaction;

import com.practice.hellomvc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.sql.SQLException;

import static com.practice.hellomvc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest // @Transactional 어노테이션은 스프링 AOP를 사용하므로 스프링 컨테이너와 함께 동작해야한다.
class MemberServiceV2_3Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";
    @Autowired
    MemberRepositoryV2 memberRepository;
    @Autowired
    MemberServiceV2_3 memberService;

    @AfterEach
    void afterEach() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("AOP check")
    void isAOP(){
        // @Transactional 어노테이션이 붙은 함수는 memberService의 함수이므로 repository는 프록시 객체가 아니다.
        log.info("memberService class = {}",memberService.getClass());
        log.info("memberRepository class ={}",memberRepository.getClass());
        assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
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

    @TestConfiguration
    static class TestConfig{
        @Bean
        DataSource dataSource(){
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }
        @Bean
        PlatformTransactionManager transactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
        @Bean
        MemberRepositoryV2 memberRepository(DataSource dataSource){
            return new MemberRepositoryV2(dataSource);
        }
        @Bean
        MemberServiceV2_3 memberService(MemberRepositoryV2 memberRepository){
            return new MemberServiceV2_3(memberRepository);
        }
    }
}