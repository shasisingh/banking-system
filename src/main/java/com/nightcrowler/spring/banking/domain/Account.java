package com.nightcrowler.spring.banking.domain;

import com.nightcrowler.spring.banking.exception.InsufficientFoundInAccountException;
import com.nightcrowler.spring.banking.exception.InvalidValueInAccountException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;


@Data
@Entity
@Table(name = "account")
@Log4j2
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "account_number", length = 32, nullable = false)
    private String accountNumber;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "account_type", length = 30)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "amount")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public enum AccountType {
        SAVINGS, CURRENT, FIXED_DEPOSIT
    }


    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            log.trace("Credited {} to account {}. New balance: {}", amount, accountNumber, balance);
        } else {
            log.info("Invalid amount to credit.");
            throw new InvalidValueInAccountException("Invalid amount to credit.");
        }

    }

    public void debit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueInAccountException("Invalid amount to debit.");
        }

        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFoundInAccountException("Insufficient funds for the transaction.");
        }

        balance = balance.subtract(amount);
        log.trace("Debited {} from account {}. New balance: {}", amount, accountNumber, balance);

    }


}
