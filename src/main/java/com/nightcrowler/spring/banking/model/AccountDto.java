package com.nightcrowler.spring.banking.model;

import com.nightcrowler.spring.banking.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class AccountDto {
    private String accountNumber;
    private String currencyCode;
    private Account.AccountType accountType;
    private BigDecimal accountBalance = BigDecimal.ZERO;
    private String relationNumber;
    private boolean active;

    public AccountDto(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.currencyCode = account.getCurrencyCode();
        this.accountType = account.getAccountType();
        this.accountBalance = account.getBalance();
        this.relationNumber = account.getCustomer().getSiebelId();
        this.active = account.isActive();
    }

}

