package com.nightcrowler.spring.banking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InsufficientFoundInAccountException extends AccountException {
    public InsufficientFoundInAccountException(String message) {
        super(message);
    }
}
