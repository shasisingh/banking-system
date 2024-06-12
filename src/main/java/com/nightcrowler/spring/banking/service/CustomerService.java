package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.CustomerException;
import com.nightcrowler.spring.banking.exception.DuplicateCustomerException;
import com.nightcrowler.spring.banking.model.CustomerDto;
import com.nightcrowler.spring.banking.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerFactory customerFactory;

    public CustomerService(CustomerRepository customerRepository, CustomerFactory customerFactory) {
        this.customerRepository = customerRepository;
        this.customerFactory = customerFactory;
    }

    public Optional<Customer> getActiveCustomer(String siebelId) {
        return getCustomerByActiveStatus(siebelId, true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {DataAccessException.class, CustomerException.class})
    public CustomerDto updateCustomerStatus(String customerId, boolean active) {
        log.atDebug()
                .addKeyValue("customerId", customerId)
                .addKeyValue("active", active)
                .log("Updating customer status");
        return customerFactory.updateCustomerStatus(customerId, active);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {DataAccessException.class, CustomerException.class})
    public CustomerDto createCustomer(CustomerDto customer) {
        log.atDebug()
                .addKeyValue("customer", customer)
                .log("Creating customer");
        validateInput(customer);
        return customerFactory.createCustomer(customer);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {DataAccessException.class, CustomerException.class})
    public CustomerDto updateCustomer(String siebelId, CustomerDto customer) {

        log.atDebug()
                .addKeyValue("siebelId", siebelId)
                .addKeyValue("customer", customer)
                .log("Updating customer");

        return customerFactory.updateCustomer(siebelId, customer);
    }


    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(CustomerDto::new)
                .toList();
    }

    private Optional<Customer> getCustomerByActiveStatus(String siebelId, boolean active) {
        return customerRepository.findBySiebelIdAndActive(siebelId, active);
    }


    private void validateInput(final CustomerDto customer) {
        Assert.isTrue(isNotValidString(customer.getSiebelId()), "Customer UniqueRef must be present and valid.");
        if (this.canCustomerExists(customer.getSiebelId())) {
            throw new DuplicateCustomerException("Customer %s already exists".formatted(customer.getSiebelId()));
        }

        Assert.isTrue(isNotValidString(customer.getFirstName()), "You must provide a valid First Name");

        Assert.isTrue(isNotValidString(customer.getCountryCode()), "You must provide a valid Country Code");
    }

    private static boolean isNotValidString(final String value) {
        return !(Objects.isNull(value) || value.isBlank());
    }

    private boolean canCustomerExists(final String siebelId) {
        return customerRepository.existsBySiebelId(siebelId);
    }

}
