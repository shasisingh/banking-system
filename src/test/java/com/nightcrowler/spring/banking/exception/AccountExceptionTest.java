package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class AccountExceptionTest {

    @Test
    void testAccountException() {
        AccountException accountException = new AccountException("Account not found");
        assertEquals("Account not found", accountException.getMessage(), "Account not found");
    }

}
