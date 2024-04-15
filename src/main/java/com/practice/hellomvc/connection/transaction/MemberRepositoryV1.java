package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class MemberRepositoryV1 {
    private final DataSource dataSource;

    public Member findById(Connection conn, String memberId) throws SQLException {
        String sql = "select * from member where member_id=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
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

    public void update(Connection conn, String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";
        PreparedStatement pstmt = null;

        try{
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
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(conn);
    }
}
