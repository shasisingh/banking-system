package com.nightcrowler.spring.banking.model;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.domain.Customer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class AccountDtoTest {

    @Test
    void testAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("1");
        accountDto.setCurrencyCode("USD");
        accountDto.setRelationNumber("Shashi");
        accountDto.setAccountBalance(new BigDecimal("1000"));
        accountDto.setAccountType(Account.AccountType.SAVINGS);
        assertEquals("1", accountDto.getAccountNumber(), "Account number is 1");
        assertEquals("Shashi", accountDto.getRelationNumber(), "Account holder name is John Doe");
        assertEquals("1000", accountDto.getAccountBalance().toEngineeringString(), "Balance is 1000");
        assertEquals(Account.AccountType.SAVINGS, accountDto.getAccountType(), "Account type is Savings");
    }

    @Test
    void testAccountDtoViaAccountObject() {
        AccountDto accountDto = new AccountDto(getAccount());
        assertEquals("1", accountDto.getAccountNumber(), "Account number is 1");
        assertEquals("Shashi", accountDto.getRelationNumber(), "Account holder name is John Doe");
        assertEquals("1000", accountDto.getAccountBalance().toEngineeringString(), "Balance is 1000");
        assertEquals(Account.AccountType.SAVINGS, accountDto.getAccountType(), "Account type is Savings");

    }

    private Account getAccount() {
        Account account = new Account();
        account.setAccountNumber("1");
        account.setCurrencyCode("USD");
        account.setCustomer(getCustomer());
        account.setBalance(new BigDecimal("1000"));
        account.setAccountType(Account.AccountType.SAVINGS);
        return account;
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setSiebelId("Shashi");
        return customer;
    }
}
