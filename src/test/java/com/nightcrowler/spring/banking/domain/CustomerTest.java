package com.nightcrowler.spring.banking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CustomerTest {

    @Test
    void testCustomer() {
        var customer = new Customer();
        customer.setSiebelId("1234567890");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("");
        customer.setCustomerType(Customer.CustomerType.INDIVIDUAL);
        customer.setActive(true);
        customer.setCountryCode("USA");

        assertEquals("1234567890", customer.getSiebelId(), "Siebel ID should be 1234567890");
        assertEquals("John", customer.getFirstName(), "First name should be John");
        assertEquals("Doe", customer.getLastName(), "Last name should be Doe");
        assertEquals("", customer.getEmail(), "Email should be empty");
        assertEquals(Customer.CustomerType.INDIVIDUAL, customer.getCustomerType(), "Customer type should be INDIVIDUAL");
        assertTrue(customer.isActive(), "Customer should be active");
        assertEquals("USA", customer.getCountryCode(), "Country code should be USA");
    }

}
