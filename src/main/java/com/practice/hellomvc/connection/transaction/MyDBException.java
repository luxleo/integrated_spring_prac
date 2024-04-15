package com.practice.hellomvc.connection.transaction;

public class MyDBException extends RuntimeException{
    public MyDBException(String message) {
        super(message);
    }

    public MyDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDBException(Throwable cause) {
        super(cause);
    }

}
