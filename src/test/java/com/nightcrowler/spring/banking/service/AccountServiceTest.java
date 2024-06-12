package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.AccountNotFoundException;
import com.nightcrowler.spring.banking.exception.ActiveCustomerNotFoundException;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.MoneyTransferDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountFactory accountFactory;
    @Mock
    private CustomerService customerService;


    @Test
    void testAddNewAccount() {
        when(customerService.getActiveCustomer(anyString())).thenReturn(Optional.of(getCustomer()));
        when(accountFactory.create(any(), any())).thenReturn(getAccount("1234567890"));
        var newAccountDto = accountService.addAccount("siebelId", new AccountDto());
        assertNotNull(newAccountDto, "Account should not be null");
    }

    @Test
    @DisplayName("Test when customer is not found")
    void testAddNewAccountCustomerNotFound() {
        when(customerService.getActiveCustomer(anyString())).thenReturn(Optional.empty());
        assertThrows(ActiveCustomerNotFoundException.class, () -> accountService.addAccount("siebelId", new AccountDto()));
    }

    @Test
    void testGetCustomerAccount(){
        when(accountRepository.findAccountByAccountIdAndCustomerSiebelId(any(), anyString())).thenReturn(Optional.of(getAccount("1234567890")));
        var accountDto = accountService.getCustomerAccount("siebelId", 1L);
        assertNotNull(accountDto, "Account should not be null");
    }

    @Test
    @DisplayName("Test when account is not found")
    void testGetCustomerAccountNotFound(){
        when(accountRepository.findAccountByAccountIdAndCustomerSiebelId(any(), anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getCustomerAccount("siebelId", 1L));
    }

    @Test
    @DisplayName("Test deactivate account")
    void testDeactivateAccount(){
        when(accountRepository.findAccountByAccountIdAndCustomerSiebelId(any(), anyString())).thenReturn(Optional.of(getAccount("1234567890")));
        when(accountFactory.inactive(any(), anyBoolean())).thenReturn(getAccount("1234567890"));
        var accountDto = accountService.deactivateAccount(1L, "siebelId", false);
        assertNotNull(accountDto, "Account should not be null");
    }

    @Test
    @DisplayName("Test when account is not found while deactivating account")
    void testDeactivateAccountNotFound(){
        when(accountRepository.findAccountByAccountIdAndCustomerSiebelId(any(), anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.deactivateAccount(1L, "siebelId", false));
    }

    @Test
    @DisplayName("Test get accounts by siebel id")
    void testGetAccountsBySiebelId(){
        when(accountRepository.findAccountByCustomerSiebelIdOrderByAccountNumber(anyString())).thenReturn(List.of(getAccount("1234567890")));
        var accountDtos = accountService.getAccountsBySiebelId("siebelId");
        assertNotNull(accountDtos, "Account should not be null");
    }


    private Account getAccount(String accountNumber) {
        var account = new Account();
        account.setBalance(new BigDecimal(1000));
        account.setCustomer(getCustomer());
        account.setAccountNumber(accountNumber);
        return account;
    }

    private MoneyTransferDto getMoneyTransferDto() {
        var moneyTransferDto = new MoneyTransferDto();
        moneyTransferDto.setAmount(new BigDecimal(100));
        moneyTransferDto.setCreditor(getAccountDto("1234567890", new BigDecimal(1000)));
        moneyTransferDto.setDebtor(getAccountDto("0987654321", new BigDecimal(1000)));
        return moneyTransferDto;
    }

    private Customer getCustomer() {
        var customer = new Customer();
        customer.setSiebelId("1234567890");
        return customer;
    }

    private AccountDto getAccountDto(String accountNumber, BigDecimal balance) {
        var accountDto = new AccountDto();
        accountDto.setAccountNumber(accountNumber);
        accountDto.setCurrencyCode("EUR");
        accountDto.setRelationNumber("1234567890");
        accountDto.setAccountType(Account.AccountType.FIXED_DEPOSIT);
        accountDto.setAccountBalance(balance);
        accountDto.setActive(true);
        return accountDto;
    }

}
