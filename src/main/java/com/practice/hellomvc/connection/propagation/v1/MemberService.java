package com.practice.hellomvc.connection.propagation.v1;

import com.practice.hellomvc.connection.propagation.Log;
import com.practice.hellomvc.connection.propagation.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    public void joinV1(String username){
        log.info("=============call memberRepo.save");
        memberRepository.save(Member.builder().username(username).build());
        log.info("=============finish memberRepo.save");

        log.info("=============call logRepo.save");
        logRepository.save(Log.builder().message(username).build());
        log.info("=============finish logRepo.save");
    }

    public void joinV2(String username){
        log.info("=============call memberRepo.save");
        memberRepository.save(Member.builder().username(username).build());
        log.info("=============finish memberRepo.save");

        log.info("=============call logRepo.save");
        try {
            logRepository.save(Log.builder().message(username).build());
        } catch (RuntimeException e){
            log.info("failed to save log message = {}",username);
            log.info("정상 흐름 반환");
        }
        log.info("=============finish logRepo.save");
    }
}
