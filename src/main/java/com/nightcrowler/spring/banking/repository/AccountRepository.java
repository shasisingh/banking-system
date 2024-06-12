package com.nightcrowler.spring.banking.repository;

import com.nightcrowler.spring.banking.domain.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends ListCrudRepository<Account, Long> {

    List<Account> findAccountByCustomerSiebelIdOrderByAccountNumber(String siebelId);

    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber AND a.currencyCode = :currencyCode AND a.customer.siebelId = :siebelId")
    Optional<Account> findAccountByAccountNumberCurrencyCodeAndSiebelId(final String accountNumber, final String currencyCode, final String siebelId);

    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber AND a.currencyCode = :currencyCode AND a.customer.siebelId = :siebelId AND a.active = true")
    Optional<Account> findActiveAccount(String accountNumber, String currencyCode, String siebelId);

    Optional<Account> findAccountByAccountIdAndCustomerSiebelId(Long accountId, String siebelId);
}
