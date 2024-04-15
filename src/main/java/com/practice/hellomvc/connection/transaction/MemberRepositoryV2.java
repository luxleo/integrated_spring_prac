package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * datasource로 부터 바로 커넥션을 획득하지 않고, 트랜잭션 매니저와 트랜잭션 동기화 매니저를 통하여 리소스 동기화와 관리를 한다.
 */
@RequiredArgsConstructor
public class MemberRepositoryV2 {
    private final DataSource dataSource;

    public Member findById( String memberId) throws SQLException {
        String sql = "select * from member where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs =  pstmt.executeQuery();

            if (rs.next()) {
                Member findMember = new Member(rs.getString("member_id"), rs.getInt("money"));
                return findMember;
            } else {
                throw new NoSuchElementException("no such member");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member (member_id, money) values(?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    public void update( String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        } finally {
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    /**
     * spring transaction manager를 이용한 트랜잭션 동기화를 위해서 DataSourceUtils를 이용한다.
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        //트랜잭션 동기화를 위해서 DataSourceUtils를 이용한다. -> 현재 트랜잭션을 사용하는 메서드가 있으면 conn은 release하지 않는다.
        DataSourceUtils.releaseConnection(conn,dataSource);
    }
}
