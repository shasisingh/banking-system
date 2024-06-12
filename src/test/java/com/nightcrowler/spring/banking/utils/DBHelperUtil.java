package com.nightcrowler.spring.banking.utils;

import com.github.javafaker.Faker;
import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.repository.AccountRepository;
import com.nightcrowler.spring.banking.repository.CustomerRepository;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public class DBHelperUtil {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    @Getter
    private final Faker faker = new Faker();

    public DBHelperUtil(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public Customer createCustomer(String siebelId) {
        var customer = new Customer();
        customer.setActive(true);
        customer.setSiebelId(siebelId);
        customer.setCountryCode(faker.address().countryCode());
        customer.setCustomerType(Customer.CustomerType.INDIVIDUAL);
        customer.setEmail(faker.internet().emailAddress());
        customer.setFirstName(faker.name().firstName());
        customer.setLastName(faker.name().lastName());
        return customerRepository.save(customer);
    }

    public Customer createCustomer() {
        var customer = new Customer();
        customer.setActive(true);
        customer.setSiebelId(String.valueOf(faker.number().numberBetween(2, 30)));
        customer.setCountryCode(faker.address().countryCode());
        customer.setCustomerType(Customer.CustomerType.INDIVIDUAL);
        customer.setEmail(faker.internet().emailAddress());
        customer.setFirstName(faker.name().firstName());
        customer.setLastName(faker.name().lastName());
        return customerRepository.save(customer);
    }


    public Account createNewAccount(Customer customer, String accountNumber) {
        var account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setActive(true);
        account.setCurrencyCode(faker.currency().code());
        account.setBalance(new BigDecimal(faker.number().randomNumber(5, true)));
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    public Account createNewAccount(Customer customer) {
        var account = new Account();
        account.setAccountNumber(faker.finance().iban());
        account.setCurrencyCode(faker.currency().code());
        account.setAccountType(Account.AccountType.SAVINGS);
        account.setActive(true);
        account.setBalance(new BigDecimal(faker.number().randomNumber(5, true)));
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    public void cleanUpIfAnyDBData() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

}
