package com.practice.hellomvc.connection.transaction;

/**
 * checked exception을 언체크로 말아서 던지기 때문에 throws를 함수 메시지에 명시할 필요가 없기 때문에 가능하다.
 */
public interface MemberRepository {
    public Member save(Member member);
    public Member findById(String memberId);
    public void update(String memberId, int money);
    public void delete(String memberId);
}
