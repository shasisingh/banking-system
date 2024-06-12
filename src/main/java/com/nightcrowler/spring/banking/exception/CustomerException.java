package com.nightcrowler.spring.banking.exception;


public class CustomerException extends RuntimeException {
    public CustomerException(String message) {
        super(message);
    }
}
