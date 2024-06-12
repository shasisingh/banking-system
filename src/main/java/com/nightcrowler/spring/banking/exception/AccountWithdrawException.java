package com.nightcrowler.spring.banking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AccountWithdrawException extends TransactionException {
    public AccountWithdrawException() {
        super("Failed to withdraw money from account");
    }

    public AccountWithdrawException(String message) {
        super(message);
    }
}
