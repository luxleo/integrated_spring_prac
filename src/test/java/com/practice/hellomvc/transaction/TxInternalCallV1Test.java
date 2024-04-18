package com.practice.hellomvc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxInternalCallV1Test {
    @Autowired
    InternalCallService internalCallService;

    @Test
    @DisplayName("@Transactional 이 아닌 함수 호출시 내부에 @Transactional 있어도 무시된다. -> AOP적용할 수 없기 때문에")
    void external(){
        internalCallService.externalCall();
    }

    @Test
    void internal(){
        internalCallService.internalCall();
    }

    @TestConfiguration
    static class TestConfig{
        @Bean
        InternalCallService internalCallService(){
            return new InternalCallService();
        }
    }
    static class InternalCallService{
        public void externalCall(){
            log.info("call external");
            printTxInfo();
            internalCall();
        }

        @Transactional
        public void internalCall() {
            log.info("call internal");
            printTxInfo();
        }

        public void printTxInfo(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("isActive={}",isActive);
        }
    }
}
