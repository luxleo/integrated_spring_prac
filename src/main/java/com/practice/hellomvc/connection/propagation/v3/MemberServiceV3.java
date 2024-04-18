package com.practice.hellomvc.connection.propagation.v3;

import com.practice.hellomvc.connection.propagation.Log;
import com.practice.hellomvc.connection.propagation.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceV3 {
    private final MemberRepositoryV3 memberRepositoryV2;
    private final LogRepositoryV3 logRepositoryV2;

    @Transactional
    public void joinV1(String username){
        log.info("=============call memberRepo.save");
        memberRepositoryV2.save(Member.builder().username(username).build());
        log.info("=============finish memberRepo.save");

        log.info("=============call logRepo.save");
        logRepositoryV2.save(Log.builder().message(username).build());
        log.info("=============finish logRepo.save");
    }

    public void joinV2(String username){
        log.info("=============call memberRepo.save");
        memberRepositoryV2.save(Member.builder().username(username).build());
        log.info("=============finish memberRepo.save");

        log.info("=============call logRepo.save");
        try {
            logRepositoryV2.save(Log.builder().message(username).build());
        } catch (RuntimeException e){
            log.info("failed to save log message = {}",username);
            log.info("정상 흐름 반환");
        }
        log.info("=============finish logRepo.save");
    }
}
