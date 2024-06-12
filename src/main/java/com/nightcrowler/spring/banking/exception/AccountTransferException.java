package com.nightcrowler.spring.banking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AccountTransferException extends TransactionException{
    public AccountTransferException() {
        super("Failed to transfer money between accounts");
    }
}
