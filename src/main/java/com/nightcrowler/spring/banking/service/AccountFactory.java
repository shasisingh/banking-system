package com.nightcrowler.spring.banking.service;


import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.DuplicateAccountException;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountFactory {

    private final AccountRepository accountRepository;

    public AccountFactory(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(final AccountDto accountDto, Customer customer) {
        log.atDebug()
                .addKeyValue("accountDto", accountDto)
                .addKeyValue("customer", customer)
                .log("Creating account");

        accountRepository.findAccountByAccountNumberCurrencyCodeAndSiebelId(accountDto.getAccountNumber(), accountDto.getCurrencyCode(), customer.getSiebelId())
                .ifPresent((Account acc) -> {
                    throw new DuplicateAccountException(acc);
                });

        var account = new Account();
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setCurrencyCode(accountDto.getCurrencyCode());
        account.setAccountType(accountDto.getAccountType());
        account.setBalance(accountDto.getAccountBalance());
        account.setActive(accountDto.isActive());
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    public Account update(final AccountDto accountType, Account account) {

        log.atDebug()
                .addKeyValue("accountType", accountType)
                .addKeyValue("account", account)
                .log("Updating account");

        account.setAccountNumber(accountType.getAccountNumber());
        account.setCurrencyCode(accountType.getCurrencyCode());
        account.setAccountType(accountType.getAccountType());
        account.setBalance(accountType.getAccountBalance());
        account.setActive(accountType.isActive());
        return accountRepository.save(account);
    }

    public Account inactive(Account account, boolean status) {
        log.atDebug()
                .addKeyValue("account", account)
                .addKeyValue("status", status)
                .log("Inactivating account");

        account.setActive(status);
        return accountRepository.save(account);
    }

}
