package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class AccountTransferExceptionTest {

    @Test
    void testAccountTransferException() {
        AccountTransferException accountTransferException = new AccountTransferException();
        assertEquals("Failed to transfer money between accounts", accountTransferException.getMessage(), "Failed to transfer money between accounts");
    }

}
