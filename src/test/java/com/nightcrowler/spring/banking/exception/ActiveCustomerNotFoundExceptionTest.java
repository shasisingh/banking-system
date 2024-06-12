package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class ActiveCustomerNotFoundExceptionTest {

        @Test
        void testActiveCustomerNotFoundException() {
            ActiveCustomerNotFoundException activeCustomerNotFoundException = new ActiveCustomerNotFoundException("Active customer not found");
            assertEquals("Active customer not found", activeCustomerNotFoundException.getMessage(), "Active customer not found");
        }

        @Test
        void testActiveCustomerNotFoundExceptionDefault() {
            ActiveCustomerNotFoundException activeCustomerNotFoundException = new ActiveCustomerNotFoundException();
            assertEquals("Active customer not found", activeCustomerNotFoundException.getMessage(), "Active customer not found");
        }

}
