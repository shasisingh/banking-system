package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.exception.AccountException;
import com.nightcrowler.spring.banking.exception.AccountNotFoundException;
import com.nightcrowler.spring.banking.exception.AccountTransferException;
import com.nightcrowler.spring.banking.exception.AccountWithdrawException;
import com.nightcrowler.spring.banking.exception.TransactionException;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.AccountOperationDto;
import com.nightcrowler.spring.banking.model.MoneyTransferDto;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class AccountTransactionService {
    private final AccountRepository accountRepository;
    private final Lock reentrantLock = new ReentrantLock(true);

    public AccountTransactionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {AccountException.class, TransactionException.class, DataAccessException.class})
    public AccountDto performTransfer(final MoneyTransferDto moneyTransfer) {

        log.atDebug()
                .addKeyValue("moneyTransfer", moneyTransfer)
                .log("Performing transfer.");

        try {
            reentrantLock.lock();
            return transfer(moneyTransfer.getCreditor(), moneyTransfer.getDebtor(), moneyTransfer.getAmount())
                    .map(AccountDto::new)
                    .orElseThrow(AccountTransferException::new);
        } finally {
            reentrantLock.unlock();
        }

    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {AccountException.class, TransactionException.class, DataAccessException.class})
    public AccountDto withdraw(AccountOperationDto accountWithdrawDto) {

        log.atDebug()
                .addKeyValue("accountWithdrawDto", accountWithdrawDto)
                .setMessage("Performing withdraw.")
                .log();

        try {
            reentrantLock.lock();
            return debit(accountWithdrawDto.getOriginatorAccount(), accountWithdrawDto.getAmount())
                    .map(AccountDto::new)
                    .orElseThrow(AccountWithdrawException::new);
        } finally {
            reentrantLock.unlock();
        }
    }
    public AccountDto deposit(AccountOperationDto depositDto) {

        log.atDebug()
                .addKeyValue("depositDto", depositDto)
                .setMessage("Performing deposit.")
                .log();

        try {
            reentrantLock.lock();
            return credit(depositDto.getOriginatorAccount(), depositDto.getAmount())
                    .map(AccountDto::new)
                    .orElseThrow(AccountWithdrawException::new);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(final AccountDto accountDto) {

        log.atDebug()
                .addKeyValue("accountDto", accountDto)
                .setMessage("Getting balance for account.")
                .log();

        return accountRepository.findAccountByAccountNumberCurrencyCodeAndSiebelId(accountDto.getAccountNumber(), accountDto.getCurrencyCode(), accountDto.getRelationNumber())
                .map(Account::getBalance)
                .orElseThrow(AccountNotFoundException::new);
    }

    private Optional<Account> transfer(final AccountDto creditor, final AccountDto debtor, final BigDecimal amount) {
        debit(debtor, amount);
        return credit(creditor, amount);
    }

    private Optional<Account> credit(final AccountDto accountNumber, final BigDecimal amount) {
        return accountRepository.findActiveAccount(accountNumber.getAccountNumber(), accountNumber.getCurrencyCode(), accountNumber.getRelationNumber())
                .map((Account account) -> {
                        account.credit(amount);
                    return Optional.of(accountRepository.save(account));
                })
                .orElseThrow(AccountNotFoundException::new);
    }


    private Optional<Account> debit(final AccountDto accountNumber, final BigDecimal amount) {
        return accountRepository.findActiveAccount(accountNumber.getAccountNumber(), accountNumber.getCurrencyCode(), accountNumber.getRelationNumber())
                .map((Account account) -> {
                    account.debit(amount);
                    return Optional.of(accountRepository.save(account));
                })
                .orElseThrow(AccountNotFoundException::new);

    }


}
