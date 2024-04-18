package com.practice.hellomvc.connection.propagation.v4;

import com.practice.hellomvc.connection.propagation.v3.LogRepositoryV3;
import com.practice.hellomvc.connection.propagation.v3.MemberRepositoryV3;
import com.practice.hellomvc.connection.propagation.v3.MemberServiceV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceV4Test {
    @Autowired
    MemberServiceV4 memberService;
    @Autowired
    MemberRepositoryV4 memberRepository;
    @Autowired
    LogRepositoryV4 logRepository;

    @Test
    @DisplayName("propagation=REQUIRE_NEW로 설정하고 에러가 발생해도 memberService에서 처리해주면 롤백 되지 않는다.")
    void fail_tx_rollback_seperate(){
        String username = "fail_ex";
        memberService.joinV2(username);

        assertThat(memberRepository.findByName(username)).isPresent();
        assertThat(logRepository.findByMessage(username)).isEmpty();
    }
}