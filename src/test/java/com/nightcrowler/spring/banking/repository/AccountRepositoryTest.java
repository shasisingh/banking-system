package com.nightcrowler.spring.banking.repository;

import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.utils.DBHelperUtil;
import com.nightcrowler.spring.banking.utils.TestDbContainerInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@DisplayName("Account Repository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest  {

    @RegisterExtension
    static TestDbContainerInitializer testDbContainerInitializer = new TestDbContainerInitializer();

    @Autowired
    private AccountRepository accountRepository;

    private static DBHelperUtil DBHelperUtil;

    private Customer testCustomer;

    @BeforeAll
    public static void setUpAll(@Autowired CustomerRepository customerRepository, @Autowired AccountRepository accountRepository) {
        DBHelperUtil = new DBHelperUtil(customerRepository, accountRepository);
    }


    @BeforeEach
    public void setUp() {
        testCustomer = DBHelperUtil.createCustomer();
    }


    @Test
    void testFindAccountByCustomerSiebelIdOrderByAccountNumber() {
        var account1 = DBHelperUtil.createNewAccount(testCustomer, "1234567890");
        var account2 = DBHelperUtil.createNewAccount(testCustomer, "1234567891");
        var filterAccountOrderByAccountNumber = accountRepository.findAccountByCustomerSiebelIdOrderByAccountNumber(testCustomer.getSiebelId());

        Assertions.assertEquals(2, filterAccountOrderByAccountNumber.size(), () -> "must have valid size");
        Assertions.assertEquals(account1.getAccountNumber(), filterAccountOrderByAccountNumber.get(0).getAccountNumber(), () -> "account number must be in same order");
        Assertions.assertEquals(account2.getAccountNumber(), filterAccountOrderByAccountNumber.get(1).getAccountNumber(), () -> "account number must be in same order");
    }

    @Test
    void testFindAccountByAccountNumberCurrencyCodeAndSiebelId() {
        var account = DBHelperUtil.createNewAccount(testCustomer);
        var accountOptional = accountRepository.findAccountByAccountNumberCurrencyCodeAndSiebelId(account.getAccountNumber(), account.getCurrencyCode(), testCustomer.getSiebelId());

        Assertions.assertTrue(accountOptional.isPresent(), () -> "account %s must be present.".formatted(account.getAccountNumber()));
        Assertions.assertEquals(account.getAccountNumber(), accountOptional.get().getAccountNumber(), () -> "account number must be same");
    }

    @Test
    void testFindAccountByAccountNumberAndCustomerSiebelId() {
        var account = DBHelperUtil.createNewAccount(testCustomer);
        var accountOptional = accountRepository.findAccountByAccountIdAndCustomerSiebelId(account.getAccountId(), testCustomer.getSiebelId());

        Assertions.assertTrue(accountOptional.isPresent(), () -> "account %s must be present.".formatted(account.getAccountNumber()));
        Assertions.assertEquals(account.getAccountNumber(), accountOptional.get().getAccountNumber(), () -> "account number must be same");
    }

    @Test
    void testFindActiveAccountByCustomerSiebelId() {
        var account = DBHelperUtil.createNewAccount(testCustomer);
        var accountOptional = accountRepository.findActiveAccount(account.getAccountNumber(), account.getCurrencyCode(), testCustomer.getSiebelId());
        Assertions.assertTrue(accountOptional.isPresent(), () -> "account %s must be present.".formatted(account.getAccountNumber()));
        Assertions.assertEquals(account.getAccountNumber(), accountOptional.get().getAccountNumber(), () -> "account number must be same");
        account.setActive(false);
        accountRepository.save(account);

        accountOptional = accountRepository.findActiveAccount(account.getAccountNumber(), account.getCurrencyCode(), testCustomer.getSiebelId());
        Assertions.assertTrue(accountOptional.isEmpty(), () -> "account %s must not be present.".formatted(account.getAccountNumber()));


    }


}
