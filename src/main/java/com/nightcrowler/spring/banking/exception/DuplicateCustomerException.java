package com.nightcrowler.spring.banking.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateCustomerException extends CustomerException {
    public DuplicateCustomerException(String message) {
        super(message);
    }

    public DuplicateCustomerException() {
        super("Customer already exists");
    }
}
