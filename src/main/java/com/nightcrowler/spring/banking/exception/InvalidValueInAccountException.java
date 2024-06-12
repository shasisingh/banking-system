package com.nightcrowler.spring.banking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidValueInAccountException extends AccountException {
    public InvalidValueInAccountException(String message) {
        super(message);
    }
}
