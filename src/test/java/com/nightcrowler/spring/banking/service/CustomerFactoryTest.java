package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.ActiveCustomerNotFoundException;
import com.nightcrowler.spring.banking.model.CustomerDto;
import com.nightcrowler.spring.banking.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerFactoryTest {

    @InjectMocks
    private CustomerFactory customerFactory;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Test create customer")
    void testCreateCustomer() {
        when(customerRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var customer = customerFactory.createCustomer(getCustomerDto());
        Assertions.assertNotNull(customer, "Customer should not be null");
        Assertions.assertEquals("1234567890", customer.getSiebelId(), "Siebel Id should be 1234567890");
        Assertions.assertEquals("John", customer.getFirstName(), "First name should be John");
        Assertions.assertEquals("Doe", customer.getLastName(), "Last name should be Doe");
        verify(customerRepository)
                .save(argThat(c -> c.getSiebelId().equals("1234567890") && c.getFirstName().equals("John") && c.getLastName().equals("Doe")));
    }

    @Test
    @DisplayName("Test update customer status")
    void testUpdateCustomerStatus() {
        when(customerRepository.findBySiebelId("1234567890")).thenReturn(of(getCustomer()));
        when(customerRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var customer = customerFactory.updateCustomerStatus("1234567890", true);
        Assertions.assertNotNull(customer, "Customer should not be null");
        Assertions.assertTrue(customer.isActive(), "Customer should be active");
        verify(customerRepository).save(argThat(Customer::isActive));
    }

    @Test
    @DisplayName("Test update customer status when customer is not found must throw exception")
    void testUpdateCustomerStatusCustomerNotFound() {
        when(customerRepository.findBySiebelId("1234567890")).thenReturn(empty());
        Assertions.assertThrows(ActiveCustomerNotFoundException.class, () -> customerFactory.updateCustomerStatus("1234567890", true));
    }

    @Test
    @DisplayName("Test update customer")
    void testUpdateCustomer() {
        when(customerRepository.findBySiebelIdAndActiveTrue("1234567890")).thenReturn(of(getCustomer()));
        when(customerRepository.save(any())).thenAnswer(in -> in.getArguments()[0]);
        var customerDto= getCustomerDto();
        customerDto.setEmail("new_mail.com");

        var customer = customerFactory.updateCustomer("1234567890", customerDto);
        Assertions.assertNotNull(customer, "Customer should not be null");
        Assertions.assertEquals("1234567890", customer.getSiebelId(), "Siebel Id should be 1234567890");
        Assertions.assertEquals("John", customer.getFirstName(), "First name should be John");
        Assertions.assertEquals("Doe", customer.getLastName(), "Last name should be Doe");
        Assertions.assertEquals("new_mail.com", customer.getEmail(), "Email should be new_mail.com");
        verify(customerRepository)
                .save(argThat(c -> c.getSiebelId().equals("1234567890") && c.getFirstName().equals("John") && c.getLastName().equals("Doe") && c.getEmail().equals("new_mail.com")));
    }

    @Test
    @DisplayName("Test update customer when customer is not found must throw exception")
    void testUpdateCustomerCustomerNotFound() {
        when(customerRepository.findBySiebelIdAndActiveTrue("1234567890")).thenReturn(empty());
        Assertions.assertThrows(ActiveCustomerNotFoundException.class, () -> customerFactory.updateCustomer("1234567890", getCustomerDto()));
    }

    private CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setSiebelId("1234567890");
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setEmail(null);
        return customerDto;
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setSiebelId("1234567890");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail(null);
        return customer;
    }


}
