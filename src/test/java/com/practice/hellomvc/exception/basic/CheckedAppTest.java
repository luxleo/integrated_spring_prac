package com.practice.hellomvc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * checked exception은 throws로 무조건 명시 해주어야한다. ->
 * 이는 특정 플랫폼에 코드가 종속적이게 된다.
 * 또한 SQLException 은 데이터베이스가 내려가있는 등 어플리케이션 레벨에서 복구 불가능한 경우에도 발생한다.
 * 이는 프로그램 전체가 영향을 받는다.
 */
@Slf4j
public class CheckedAppTest {
    @Test
    void checked(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(()->controller.request())
                .isInstanceOf(Exception.class);
    }
    static class Controller {
        Service service = new Service();
        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();
        public void logic() throws ConnectException, SQLException {
            repository.call();
            networkClient.call();
        }
    }
    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException("fail to connect");
        }
    }
}
