package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.DuplicateAccountException;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountFactoryTest {

    @InjectMocks
    private AccountFactory accountFactory;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void testCreateAccount() {
        when(accountRepository.findAccountByAccountNumberCurrencyCodeAndSiebelId(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var account = accountFactory.create(getAccountDto(), getCustomer());
        assertNotNull(account, "Account must not be null");
        assertEquals(getAccountDto().getAccountNumber(), account.getAccountNumber(), "Account number must be equal");
        assertEquals(getAccountDto().getCurrencyCode(), account.getCurrencyCode(), "Currency code must be equal");
        assertEquals(getAccountDto().getAccountType(), account.getAccountType(), "Account type must be equal");
        assertEquals(getAccountDto().getAccountBalance(), account.getBalance(), "Account balance must be equal");
        assertEquals(getAccountDto().isActive(), account.isActive(), "Account active status must be equal");
        assertEquals(getCustomer().getSiebelId(), account.getCustomer().getSiebelId(), "Customer siebel id must be equal");
    }

    @Test
    void testCreateAccountWithDuplicateAccount() {
        when(accountRepository.findAccountByAccountNumberCurrencyCodeAndSiebelId(any(), any(), any()))
                .thenReturn(Optional.of(new Account()));
        assertThrows(DuplicateAccountException.class, () -> accountFactory.create(getAccountDto(), getCustomer()));
    }

    @Test
    void testUpdateAccount() {
        when(accountRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var account = accountFactory.update(getAccountDto(), new Account());
        assertNotNull(account, "Account must not be null");
        assertEquals(getAccountDto().getAccountNumber(), account.getAccountNumber(), "Account number must be equal");
        assertEquals(getAccountDto().getCurrencyCode(), account.getCurrencyCode(), "Currency code must be equal");
        assertEquals(getAccountDto().getAccountType(), account.getAccountType(), "Account type must be equal");
        assertEquals(getAccountDto().getAccountBalance(), account.getBalance(), "Account balance must be equal");
        assertEquals(getAccountDto().isActive(), account.isActive(), "Account active status must be equal");
    }

    @Test
    void testInactiveAccount() {
        when(accountRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var account = accountFactory.inactive(new Account(), false);
        assertFalse(account.isActive(), "Account must be inactive");
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setSiebelId("123");
        return customer;
    }

    private AccountDto getAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("123");
        accountDto.setCurrencyCode("USD");
        accountDto.setAccountType(Account.AccountType.FIXED_DEPOSIT);
        accountDto.setAccountBalance(new BigDecimal("1000.0"));
        accountDto.setActive(true);
        return accountDto;
    }

}
