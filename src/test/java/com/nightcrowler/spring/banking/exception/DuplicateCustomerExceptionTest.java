package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class DuplicateCustomerExceptionTest {

        @Test
        void testDuplicateCustomerException() {
            DuplicateCustomerException duplicateCustomerException = new DuplicateCustomerException("Customer already exists");
            assertEquals("Customer already exists", duplicateCustomerException.getMessage(), "Customer already exists");
        }

        @Test
        void testDuplicateCustomerExceptionDefault() {
            DuplicateCustomerException duplicateCustomerException = new DuplicateCustomerException();
            assertEquals("Customer already exists", duplicateCustomerException.getMessage(), "Customer already exists");
        }

}
