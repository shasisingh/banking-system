package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class InsufficientFoundInAccountExceptionTest {

        @Test
        void testInsufficientFoundInAccountException() {
            InsufficientFoundInAccountException insufficientFoundInAccountException = new InsufficientFoundInAccountException("Insufficient funds in account");
            assertEquals("Insufficient funds in account", insufficientFoundInAccountException.getMessage(), "Insufficient funds in account");
        }


}
