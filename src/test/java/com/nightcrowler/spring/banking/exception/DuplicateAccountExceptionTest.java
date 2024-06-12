package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class DuplicateAccountExceptionTest {

        @Test
        void testDuplicateAccountException() {
            DuplicateAccountException duplicateAccountException = new DuplicateAccountException("Account already exists");
            assertEquals("Account already exists", duplicateAccountException.getMessage(), "Account already exists");
        }


}
