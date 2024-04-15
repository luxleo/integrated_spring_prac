package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/**
 * 트랜잭션 매니저를 이용하여 데이터 접근기술 플랫폼에 독립적으로 트랜잭션 기능 사용가능.
 * release 등의 트랜잭션 관리(커넥션) 함수를 따로 지정하지 않아도 된다.
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2_1 {
    private final PlatformTransactionManager txManager;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        // jdbc : new DefaultTransactionDefinition() , JPA: new JpaTransactionManager()
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        try{
            bizlogic( fromId, toId, amount);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
            throw new IllegalStateException(e);
        }
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
