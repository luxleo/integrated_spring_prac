package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * V1의 문제점 JDBC 누수가 있다.
 * 트랜잭션 위해 try, catch, finally 등의 중복 코드가 있다.
 * 트랜잭션을 위해서 connection 객체를 넘겨 주어야하고 repository의 경우 트랜잭션 이용 메서드와 미이용 메서드를 중복해서 작성해야한다.
 * 트랜잭션 관리 코드가 플랫폼(jdbc, jpa)마다 상이하다. OCP를 지키지 못한다.
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final DataSource dataSource;
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int amount) throws SQLException {
        Connection conn = dataSource.getConnection();
        try{
            conn.setAutoCommit(false);
            bizlogic(conn, fromId, toId, amount);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(conn);
        }

    }

    private void release(Connection conn){
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private void bizlogic(Connection conn,String fromId, String toId, int amount) throws SQLException {
        Member fromMember = memberRepository.findById(conn, fromId);
        Member toMember = memberRepository.findById(conn, toId);

        memberRepository.update(conn, fromMember.getMemberId(), fromMember.getMoney() - amount);
        validateMember(toMember.getMemberId());
        memberRepository.update(conn, toMember.getMemberId(), toMember.getMoney() + amount);
    }

    private void validateMember(String memberId) {
        if (memberId.equals("ex")) {
            throw new IllegalStateException("invalid member");
        }
    }
}
