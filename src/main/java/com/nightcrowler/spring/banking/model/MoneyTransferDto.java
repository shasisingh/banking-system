package com.nightcrowler.spring.banking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class MoneyTransferDto {
    private AccountDto creditor;
    private AccountDto debtor;
    private BigDecimal amount;

    public MoneyTransferDto(AccountDto creditor, AccountDto debtor, BigDecimal amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }

}

