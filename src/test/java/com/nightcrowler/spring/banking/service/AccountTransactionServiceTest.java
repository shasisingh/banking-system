package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.AccountNotFoundException;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.AccountOperationDto;
import com.nightcrowler.spring.banking.model.MoneyTransferDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTransactionServiceTest {

    @InjectMocks
    private AccountTransactionService accountTransactionService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Test perform transfer from account 0987654321 to 1234567890 as EUR 100")
    void testPerformTransfer() {
        when(accountRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var accountInTable1 = getAccount("1234567890");
        var accountInTable2 = getAccount("0987654321");
        when(accountRepository.findActiveAccount(eq("1234567890"), anyString(), anyString())).thenReturn(Optional.of(accountInTable1));
        when(accountRepository.findActiveAccount(eq("0987654321"), anyString(), anyString())).thenReturn(Optional.of(accountInTable2));
        var output = accountTransactionService.performTransfer(getMoneyTransferDto());
        assertNotNull(output, "Output should not be null");
        assertEquals(new BigDecimal(1100), output.getAccountBalance(), "Account balance should be 1100");
        assertEquals("1234567890", output.getRelationNumber(), "Account number should be 1234567890");

        verify(accountRepository, times(1))
                .save(argThat(account -> (account.getBalance().equals(new BigDecimal(1100)) && account.getAccountNumber().equals("1234567890")))
                );

        verify(accountRepository, times(1))
                .save(argThat(account -> (account.getBalance().equals(new BigDecimal(900)) && account.getAccountNumber().equals("0987654321")))
                );


    }

    @Test
    @DisplayName("Test When Account Not Exists or Inactive")
    void testPerformTransferWhenAccountNotExistsOrInactive() {
        when(accountRepository.findActiveAccount(any(), anyString(), anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountTransactionService.performTransfer(getMoneyTransferDto()), "AccountNotFoundException should be thrown");

    }

    @Test
    @DisplayName("Test Account Withdrawing")
    void testWithdraw() {
        when(accountRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var accountInTable = getAccount("1234567890");
        accountInTable.setBalance(new BigDecimal(10000));
        when(accountRepository.findActiveAccount(eq("1234567890"), anyString(), anyString())).thenReturn(Optional.of(accountInTable));
        var output = accountTransactionService.withdraw(new AccountOperationDto(getAccountDto("1234567890",new BigDecimal(100)), new BigDecimal(100)));
        assertNotNull(output, "Output should not be null");
        assertEquals(new BigDecimal(9900), output.getAccountBalance(), "Account balance should be 9900");
        assertEquals("1234567890", output.getRelationNumber(), "Account number should be 1234567890");

        verify(accountRepository, times(1))
                .save(argThat(account -> (account.getBalance().equals(new BigDecimal(9900)) && account.getAccountNumber().equals("1234567890")))
                );

    }

    @Test
    @DisplayName("Test Account Depositing")
    void testDeposit() {
        when(accountRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var accountInTable = getAccount("1234567890");
        accountInTable.setBalance(new BigDecimal(0));
        when(accountRepository.findActiveAccount(eq("1234567890"), anyString(), anyString())).thenReturn(Optional.of(accountInTable));
        var output = accountTransactionService.deposit(new AccountOperationDto(getAccountDto("1234567890",new BigDecimal(100)), new BigDecimal(100)));
        assertNotNull(output, "Output should not be null");
        assertEquals(new BigDecimal(100), output.getAccountBalance(), "Account balance should be 100");
        assertEquals("1234567890", output.getRelationNumber(), "Account number should be 1234567890");

        verify(accountRepository, times(1))
                .save(argThat(account -> (account.getBalance().equals(new BigDecimal(100)) && account.getAccountNumber().equals("1234567890")))
                );

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
