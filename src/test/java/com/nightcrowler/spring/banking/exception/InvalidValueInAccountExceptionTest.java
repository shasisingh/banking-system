package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class InvalidValueInAccountExceptionTest {

        @Test
        void testInvalidValueInAccountException() {
            InvalidValueInAccountException invalidValueInAccountException = new InvalidValueInAccountException("Invalid value in account");
            assertEquals("Invalid value in account", invalidValueInAccountException.getMessage(), "Invalid value in account");
        }


}
