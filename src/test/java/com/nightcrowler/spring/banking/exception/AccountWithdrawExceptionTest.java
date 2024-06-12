package com.nightcrowler.spring.banking.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AccountWithdrawExceptionTest {

    @Test
    void testAccountWithdrawException() {
        AccountWithdrawException accountWithdrawException = new AccountWithdrawException();
        Assertions.assertEquals("Failed to withdraw money from account", accountWithdrawException.getMessage(), "Failed to withdraw money from account");
    }

    @Test
    void testAccountWithdrawExceptionWithMessage() {
        AccountWithdrawException accountWithdrawException = new AccountWithdrawException("Failed to withdraw money from account");
        Assertions.assertEquals("Failed to withdraw money from account", accountWithdrawException.getMessage(), "Failed to withdraw money from account");
    }

}
