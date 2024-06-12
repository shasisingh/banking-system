package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class TransactionExceptionTest {

            @Test
            void testTransactionException() {
                TransactionException transactionException = new TransactionException("Transaction failed");
                assertEquals("Transaction failed", transactionException.getMessage(), "Transaction failed");
            }


}
