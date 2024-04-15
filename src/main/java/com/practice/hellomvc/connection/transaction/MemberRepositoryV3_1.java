package com.practice.hellomvc.connection.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV3_1 implements MemberRepository{
    private final DataSource dataSource;
    @Override
    public Member save(Member member) {
        String sql = "insert into member (member_id,money) values (?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            close(conn,pstmt,null);
        }
        return member;
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if(rs.next()){
                return new Member(rs.getString("member_id"),rs.getInt("money"));
            }else{
                throw new NoSuchElementException("cannot find such member");
            }
        } catch (SQLException e) {
            throw new MyDBException(e);
        } finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn=getConnection();
            pstmt= conn.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyDBException(e);
        }finally {
            close(conn,pstmt,null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn=getConnection();
            pstmt= conn.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyDBException(e);
        }finally {
            close(conn,pstmt,null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(conn,dataSource);
    }
    private Connection getConnection(){
        return DataSourceUtils.getConnection(dataSource);
    }
}
