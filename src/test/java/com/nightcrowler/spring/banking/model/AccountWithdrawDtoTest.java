package com.nightcrowler.spring.banking.model;

import com.nightcrowler.spring.banking.domain.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
class AccountWithdrawDtoTest {

    @Test
    void testAccountWithdrawDto() {
        AccountOperationDto accountWithdrawDto = new AccountOperationDto();
        accountWithdrawDto.setAmount(new BigDecimal("100.01"));
        accountWithdrawDto.setOriginatorAccount(getAccountDto());
        assertEquals("100.01", accountWithdrawDto.getAmount().toEngineeringString(), "value mismatch");
        assertEquals("ACCC1", accountWithdrawDto.getOriginatorAccount().getAccountNumber(), "value mismatch");
    }

    AccountDto getAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setRelationNumber("shashi");
        accountDto.setAccountNumber("ACCC1");
        accountDto.setAccountType(Account.AccountType.SAVINGS);
        accountDto.setAccountBalance(new BigDecimal("100.01"));
        return accountDto;
    }

}
