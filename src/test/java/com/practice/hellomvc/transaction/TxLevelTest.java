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
@Transactional(readOnly = true)
@SpringBootTest
public class TxLevelTest {
    @Autowired
    LevelService service;

    @Test
    void write(){
        service.write();
    }

    @Test
    void read(){
        service.read();
    }

    @TestConfiguration
    static class TestConfig{
        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }
    static class LevelService{

        @Transactional(readOnly = false)
        public void write(){
            log.info("write function is not read only");
            printTxInfo();
        }

        public void read(){
            log.info("read function is read only");
            printTxInfo();
        }
        public void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}",txActive);
            boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("is read only = {}",isReadOnly);
        }
    }
}
