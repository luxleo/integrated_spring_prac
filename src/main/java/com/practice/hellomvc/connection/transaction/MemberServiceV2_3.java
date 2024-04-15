package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * @Transactional 어노테이션으로 프록시 객체로 부터 호출한다. -> 서비스 계층에서 트랜잭션 관리부를 완전히 제거할 수 있다.
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2_3 {
    private final MemberRepositoryV2 memberRepository;

    @Transactional
    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        bizlogic(fromId, toId, amount);
    }

    private void bizlogic(String fromId, String toId, int amount) throws SQLException {
        Member fromMember = memberRepository.findById( fromId);
        Member toMember = memberRepository.findById( toId);

        memberRepository.update(fromMember.getMemberId(), fromMember.getMoney() - amount);
        validateMember(toMember.getMemberId());
        memberRepository.update(toMember.getMemberId(), toMember.getMoney() + amount);
    }

    private void validateMember(String memberId) {
        if (memberId.equals("ex")) {
            throw new IllegalStateException("invalid member");
        }
    }
}
