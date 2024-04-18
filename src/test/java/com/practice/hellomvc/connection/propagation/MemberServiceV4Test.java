package com.practice.hellomvc.connection.propagation;

import com.practice.hellomvc.connection.propagation.v2.LogRepositoryV2;
import com.practice.hellomvc.connection.propagation.v2.MemberRepositoryV2;
import com.practice.hellomvc.connection.propagation.v2.MemberServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

/**
 * 현재는 트랜잭션이 MemberService에서 전파되지 않고 각각 일어나고 있다.
 */
@Slf4j
@SpringBootTest
class MemberServiceV4Test {
    @Autowired
    MemberRepositoryV2 memberRepositoryV2;
    @Autowired
    LogRepositoryV2 logRepositoryV2;
    @Autowired
    MemberServiceV2 memberService;

    @Test
    void outerTx_off_success(){
        String username = "outer_success";

        memberService.joinV1(username);

        assertThat(memberRepositoryV2.findByName(username)).isPresent();
        assertThat(logRepositoryV2.findByMessage(username)).isPresent();
    }

    @Test
    @DisplayName("서비스계층에 @Transactional로 관리 되지 않는경우 롤백이 일어나도 논리 트랜잭션으로 관리 되지 않아 각각 물리 트랜잭션으로 커밋, 롤백이 일어난다.")
    void outerTx_off_failure(){
        String username = "outer_fail_ex";

        assertThatThrownBy(()->memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        assertThat(memberRepositoryV2.findByName(username)).isPresent();
        assertThat(logRepositoryV2.findByMessage(username)).isEmpty();
    }

}