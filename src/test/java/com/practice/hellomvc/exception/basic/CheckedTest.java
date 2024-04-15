package com.practice.hellomvc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 메서드가 checked 예외를 잡아서 던질때는 무조건 throws를 선언해주어야한다.
 * 장점: 컴파일시 throws를 명시적으로 선언하지 않았으면 컴파일 에러가 발생한다.
 * 단점: 인터페이스등으로 추상화 할때 throws에 의존적이게 되어 코드가 오염된다.
 */
@Slf4j
public class CheckedTest {

    @Test
    void checked_catch(){
        Service service = new Service();
        service.call_catch();
    }

    @Test
    void checked_throw(){
        Service service = new Service();
        Assertions.assertThatThrownBy(()-> service.call_throw())
                .isInstanceOf(MyCheckedException.class);
    }

    static class MyCheckedException extends Exception{
        public MyCheckedException(String message) {
            super(message);
        }
    }
    static class Service {
        Repository repository = new Repository();
        public void call_catch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("예외 처리 message = {}",e.getMessage(),e);
            }
        }
        public void call_throw() throws MyCheckedException {
            repository.call();
        }
    }
    static class Repository{
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
