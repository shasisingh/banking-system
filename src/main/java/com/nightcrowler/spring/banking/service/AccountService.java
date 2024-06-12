package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.exception.AccountNotFoundException;
import com.nightcrowler.spring.banking.exception.ActiveCustomerNotFoundException;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final CustomerService customerService;

    public AccountService(AccountRepository accountRepository, AccountFactory accountFactory, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public AccountDto getCustomerAccount(final String siebelId, final Long accountId) {
        return accountRepository.findAccountByAccountIdAndCustomerSiebelId(accountId, siebelId)
                .map(AccountDto::new)
                .orElseThrow(AccountNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsBySiebelId(String siebelId) {
        return accountRepository.findAccountByCustomerSiebelIdOrderByAccountNumber(siebelId)
                .stream()
                .map(AccountDto::new)
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DataAccessException.class)
    public AccountDto addAccount(String siebelId, AccountDto account) {
        return customerService.getActiveCustomer(siebelId)
                .map(customer -> accountFactory.create(account, customer))
                .map(AccountDto::new)
                .orElseThrow(ActiveCustomerNotFoundException::new);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DataAccessException.class)
    public AccountDto deactivateAccount(Long accountId, String siebelId, boolean status) {
        return accountRepository.findAccountByAccountIdAndCustomerSiebelId(accountId, siebelId)
                .map(account -> accountFactory.inactive(account, status))
                .map(AccountDto::new)
                .orElseThrow(AccountNotFoundException::new);

    }


}
