package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.DuplicateCustomerException;
import com.nightcrowler.spring.banking.model.CustomerDto;
import com.nightcrowler.spring.banking.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerFactory customerFactory;

    @Test
    @DisplayName("Test get active customer")
    void testGetActiveCustomer() {
        when(customerRepository.findBySiebelIdAndActive(any(),anyBoolean())).thenReturn(Optional.of(getCustomer()));
        var customer = customerService.getActiveCustomer("customerSiebelId");
        assertNotNull(customer, "Customer should not be null");
    }

    @Test
    @DisplayName("Test update customer status")
    void testUpdateCustomerStatus() {
        when(customerFactory.updateCustomerStatus(any(), anyBoolean())).thenReturn(getCustomerDto());
        var customer = customerService.updateCustomerStatus("customerId", true);
        assertNotNull(customer, "Customer should not be null");
    }


    @Test
    @DisplayName("Test create customer")
    void testCreateCustomerThrowErrorWhenCountryCodeNotProvided() {
          CustomerDto customerDto = getCustomerDto();
            customerDto.setCountryCode(null);
            assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(customerDto));
    }

    @Test
    @DisplayName("Test create customer thore error when firstname is not provided")
    void testCreateCustomerThrowErrorWhenFirstNameNotProvided() {
        CustomerDto customerDto = getCustomerDto();
        customerDto.setFirstName(null);
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(customerDto));
    }

    @Test
    @DisplayName("Test create customer thore error when siebelId is not provided")
    void testCreateCustomerThrowErrorWhenSiebelIdNotProvided() {
        CustomerDto customerDto = getCustomerDto();
        customerDto.setSiebelId(null);
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(customerDto));
    }

    @Test
    @DisplayName("Test create customer thore error when account already exists")
    void testCreateCustomerThrowErrorWhenAccountAlreadyExists() {
        CustomerDto customerDto = getCustomerDto();
        when(customerRepository.existsBySiebelId(customerDto.getSiebelId())).thenReturn(true);
        assertThrows(DuplicateCustomerException.class, () -> customerService.createCustomer(customerDto));
    }

    @Test
    @DisplayName("Test create customer")
    void testCreateCustomer() {
        CustomerDto customerDto = getCustomerDto();
        customerDto.setCountryCode("EUR");
        when(customerFactory.createCustomer(any())).thenReturn(customerDto);
        var customer = customerService.createCustomer(customerDto);
        assertNotNull(customer, "Customer should not be null");
    }

    @Test
    @DisplayName("Test update customer")
    void testUpdateCustomer() {
        when(customerFactory.updateCustomer(any(), any())).thenReturn(getCustomerDto());
        var customer = customerService.updateCustomer("siebelId", getCustomerDto());
        assertNotNull(customer, "Customer should not be null");
    }

    @Test
    @DisplayName("Test get all customers")
    void getAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(getCustomer()));
        var customers = customerService.getAllCustomers();
        assertNotNull(customers, "Customers should not be null");
        assertEquals(1, customers.size(), "Customers size should be 1");
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setSiebelId("1234567890");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail(null);
        return customer;
    }

    private CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setSiebelId("1234567890");
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setEmail(null);
        return customerDto;
    }
}
