package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class AccountNotFoundExceptionTest {

    @Test
    void testAccountNotFoundException() {
        AccountNotFoundException accountNotFoundException = new AccountNotFoundException("Account not found");
        assertEquals("Account not found", accountNotFoundException.getMessage(), "Account not found");
    }

    @Test
    void testAccountNotFoundExceptionDefault() {
        AccountNotFoundException accountNotFoundException = new AccountNotFoundException();
        assertEquals("Account not found", accountNotFoundException.getMessage(), "Account not found");
    }

}
