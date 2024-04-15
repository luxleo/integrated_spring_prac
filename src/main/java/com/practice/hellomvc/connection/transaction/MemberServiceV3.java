package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 언체크드로 체크드를 감싸서 던져주기 때문에 인터페이스로 추상화 할 수 있어서
 * 완전한 DI를 지킬수 있다.
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3 {
    private final MemberRepository memberRepository;

    @Transactional
    public void accountTransfer(String fromId, String toId, int amount) {
        bizlogic(fromId, toId, amount);
    }

    private void bizlogic(String fromId, String toId, int amount) {
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
