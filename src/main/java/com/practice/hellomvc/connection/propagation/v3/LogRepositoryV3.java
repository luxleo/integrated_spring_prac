package com.practice.hellomvc.connection.propagation.v3;

import com.practice.hellomvc.connection.propagation.Log;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepositoryV3 {
    private final EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Log logMessage){
        log.info("save log = {}",log);
        if(logMessage.getMessage().contains("ex")){
            log.info("exception raised in logRepository");
            throw new RuntimeException("not invalid username");
        }
        em.persist(logMessage);
    }
    public Optional<Log> findByMessage(String message){
        return em.createQuery("select l from Log l where l.message=:message",Log.class)
                .setParameter("message",message)
                .getResultList().stream().findAny();
    }
}
