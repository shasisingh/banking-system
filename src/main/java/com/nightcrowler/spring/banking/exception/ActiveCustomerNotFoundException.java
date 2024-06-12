package com.nightcrowler.spring.banking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ActiveCustomerNotFoundException extends CustomerException {
    public ActiveCustomerNotFoundException(String message) {
        super(message);
    }

    public ActiveCustomerNotFoundException() {
        super("Active customer not found");
    }
}
