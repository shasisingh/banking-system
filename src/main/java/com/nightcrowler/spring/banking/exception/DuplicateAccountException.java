package com.nightcrowler.spring.banking.exception;

import com.nightcrowler.spring.banking.domain.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateAccountException extends AccountException {
    public DuplicateAccountException(Account account) {
        super("Account %s already exists".formatted(account.getAccountNumber()));
    }

    public DuplicateAccountException(String message) {
        super(message);
    }

}
