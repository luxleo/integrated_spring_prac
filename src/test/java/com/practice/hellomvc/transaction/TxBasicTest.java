package com.practice.hellomvc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {
    @Autowired
    TxService txService;

    @Test
    void isTx(){
        txService.tx();
    }

    @Test
    void noneTx(){
        txService.nonTx();
    }

    @TestConfiguration
    static class TestConfig{
        @Bean
        TxService txService(){
            return new TxService();
        }

    }
    static class TxService{
        @Transactional
        public void tx(){
            log.info("it's transaction function");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}",txActive);

        }

        public void nonTx(){
            log.info("it's none transaction function");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}",txActive);
        }
    }
}
