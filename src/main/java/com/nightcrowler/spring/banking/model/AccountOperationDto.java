package com.nightcrowler.spring.banking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperationDto {
    private AccountDto originatorAccount;
    private BigDecimal amount;
}
