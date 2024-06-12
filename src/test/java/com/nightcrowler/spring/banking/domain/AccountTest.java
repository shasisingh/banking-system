package com.nightcrowler.spring.banking.domain;

import com.nightcrowler.spring.banking.exception.InsufficientFoundInAccountException;
import com.nightcrowler.spring.banking.exception.InvalidValueInAccountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Account Test")
class AccountTest {

    @Test
    void testAccount() {
        var account = new Account();
        account.setAccountNumber("1234567890");
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setBalance(new BigDecimal("1000.0"));
        account.setCustomer(new Customer());
        account.setActive(true);
        account.setCustomer(new Customer());
        account.setCurrencyCode("USD");

        assertEquals("1234567890", account.getAccountNumber(), "Account number should be 1234567890");
        assertEquals(Account.AccountType.SAVINGS, account.getAccountType(), "Account type should be SAVINGS");
        assertEquals(new BigDecimal("1000.0"), account.getBalance(), "Balance should be 1000.0");
        assertNotNull(account.getCustomer(), "Customer should not be null");
        assertTrue(account.isActive(), "Account should be active");
        assertEquals("USD", account.getCurrencyCode(), "Currency code should be USD");

    }

    @Test
    void testCredit() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));
        account.credit(new BigDecimal("100.0"));
        assertEquals(new BigDecimal("1100.0"), account.getBalance(), "Balance should be 1100.0");
    }

    @Test
    void testDebit() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));
        account.debit(new BigDecimal("100.0"));
        assertEquals(new BigDecimal("900.0"), account.getBalance(), "Balance should be 900.0");
    }

    @Test
    void testDebitWithInsufficientFoundInAccountException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));

        assertThatThrownBy(() -> account.debit(new BigDecimal("1100.0")))
                .isInstanceOf(InsufficientFoundInAccountException.class)
                .hasMessage("Insufficient funds for the transaction.");
    }

    @Test
    void testDebitWithInvalidValueInAccountException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));

        assertThatThrownBy(() -> account.debit(new BigDecimal("-100.0")))
                .isInstanceOf(InvalidValueInAccountException.class)
                .hasMessage("Invalid amount to debit.");
    }

    @Test
    void testDebitWithInvalidValueInAccountExceptionWithZero() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));

        assertThatThrownBy(() -> account.debit(new BigDecimal("0.0")))
                .isInstanceOf(InvalidValueInAccountException.class)
                .hasMessage("Invalid amount to debit.");

    }

    @Test
    void testCreditWithInvalidValueInAccountException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));

        assertThatThrownBy(() -> account.credit(new BigDecimal("-100.0")))
                .isInstanceOf(InvalidValueInAccountException.class)
                .hasMessage("Invalid amount to credit.");

    }

    @Test
    void testCreditWithInvalidValueInAccountExceptionWithZero() {
        Account account = new Account();
        account.setBalance(new BigDecimal("1000.0"));
        assertThatThrownBy(() -> account.credit(new BigDecimal("0.0")))
                .isInstanceOf(InvalidValueInAccountException.class)
                .hasMessage("Invalid amount to credit.");

    }

}
