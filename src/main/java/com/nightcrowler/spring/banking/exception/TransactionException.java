package com.nightcrowler.spring.banking.exception;


public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }

}
