package com.practice.hellomvc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class TxPropagationTest {
    @Autowired
    PlatformTransactionManager txManager;

    @Test
    @DisplayName("외부가 내부를 감싸지 않으면 트랜잭션 전파하지않는다.")
    void two_commit(){
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());
        log.info("outer is new Transaction = {}",outer.isNewTransaction());
        txManager.commit(outer);

        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionDefinition());
        log.info("inner is new Transaction = {}",inner.isNewTransaction());
        txManager.commit(inner);
    }

    @Test
    @DisplayName("논리 트랜잭션 중 하나라도 rollback -> 전체 물리 트랜잭션 롤백, 모든 논리 트랜잭션 commit -> 물리 트랜잭션 commit")
    void outer_commit_inner_rollback(){
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());
        log.info("outer is new tx={}",outer.isNewTransaction());

        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionDefinition());
        log.info("inner is new tx={}",inner.isNewTransaction());

        txManager.rollback(inner);

        Assertions.assertThatThrownBy(()->txManager.commit(outer))
                        .isInstanceOf(UnexpectedRollbackException.class);
    }

    @TestConfiguration
    static class TestConfig{
        @Bean
        PlatformTransactionManager transactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }
}
