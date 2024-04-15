package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 템플릿을 이용하여 중복되는 try catch 내에서 commit, rollback의 함수 코드 중복을 없앰.
 */
@Slf4j
public class MemberServiceV2_2 {
    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV2 memberRepository;

    public MemberServiceV2_2(PlatformTransactionManager txManager, MemberRepositoryV2 memberRepository) {
        this.txTemplate = new TransactionTemplate(txManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        txTemplate.executeWithoutResult((transactionStatus -> {
            try{
                bizlogic(fromId, toId, amount);
            } catch (SQLException e){
                // checked 예외를 해당 람다안에서 던질수 없기 때문에, 언체크드로 타입변환하여 던진다.
                throw new IllegalStateException(e);
            }
        }));
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
