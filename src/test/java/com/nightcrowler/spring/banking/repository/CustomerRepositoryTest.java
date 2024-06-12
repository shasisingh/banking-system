package com.nightcrowler.spring.banking.repository;

import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.utils.DBHelperUtil;
import com.nightcrowler.spring.banking.utils.TestDbContainerInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Customer Repository Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest  {

    @RegisterExtension
    static TestDbContainerInitializer testDbContainerInitializer = new TestDbContainerInitializer();

    @Autowired
    private CustomerRepository customerRepository;

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
    void testFindBySiebelIdAndActiveTrue() {
        var customer = customerRepository.findBySiebelIdAndActiveTrue(testCustomer.getSiebelId());
        assertTrue(customer.isPresent(), "Customer must be present");
        assertEquals(testCustomer.getSiebelId(), customer.get().getSiebelId(), "Customer siebel id must be same");
    }

    @Test
    void testFindBySiebelIdAndActive() {
        var customer = customerRepository.findBySiebelIdAndActive(testCustomer.getSiebelId(), true);
        assertTrue(customer.isPresent(), "Customer must be present");
        assertEquals(testCustomer.getSiebelId(), customer.get().getSiebelId(), "Customer siebel id must be same");
    }

    @Test
    void testExistsBySiebelId() {
        assertTrue(customerRepository.existsBySiebelId(testCustomer.getSiebelId()), "Customer must exist");
    }

    @Test
    void testFindBySiebelId() {
        var customer = customerRepository.findBySiebelId(testCustomer.getSiebelId());
        assertTrue(customer.isPresent(), "Customer must be present");
        assertEquals(testCustomer.getSiebelId(), customer.get().getSiebelId(), "Customer siebel id must be same");
    }
}
