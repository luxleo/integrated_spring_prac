package com.practice.hellomvc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class UnCheckedAppTest {
    @Test
    void unchecked_throw(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(()->controller.request())
                .isInstanceOf(Exception.class);
    }
    @Test
    void unchecked_printEX(){
        Controller controller = new Controller();
        try{
            controller.request();
        } catch (Exception e){
            log.info("error",e);
        }
    }
    static class Controller{
        Service service = new Service();
        public void request(){
            service.logic();
        }
    }
    static class Service {
        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        public void logic(){
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient{
        public void call(){
            throw new RunTimeNetworkException("network err");
        }
    }
    static class Repository{
        public void call(){
            try{
                runSQL();
            } catch (SQLException e) {
                log.info("sql error occurred",e);
                throw new RunTimeSQLException(e);
            }
        }

        /**
         * 체크예외를 언체크 예외로 말아서 던질때 반드시 이전의 에러를 포함하여 던져야한다.
         * @throws SQLException
         */
        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }
    static class RunTimeSQLException extends RuntimeException{
        public RunTimeSQLException(Throwable cause) {
            super(cause);
        }
    }
    static class RunTimeNetworkException extends RuntimeException{
        public RunTimeNetworkException(String message) {
            super(message);
        }
    }
}
