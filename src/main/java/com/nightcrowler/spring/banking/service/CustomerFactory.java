package com.nightcrowler.spring.banking.service;

import com.nightcrowler.spring.banking.domain.Customer;
import com.nightcrowler.spring.banking.exception.ActiveCustomerNotFoundException;
import com.nightcrowler.spring.banking.model.CustomerDto;
import com.nightcrowler.spring.banking.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
@Slf4j
public class CustomerFactory {
    private final CustomerRepository customerRepository;

    public CustomerFactory(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDto createCustomer(CustomerDto customer) {

        log.atDebug()
                .addKeyValue("customer", customer)
                .log("Creating customer");

        return new CustomerDto(setValueIfRequired(customer, new Customer()));
    }

    public CustomerDto updateCustomer(String  siebelId, CustomerDto customer) {
        log.atDebug().addKeyValue("siebelId", siebelId).addKeyValue("customer", customer).log("Updating customer");

        return customerRepository.findBySiebelIdAndActiveTrue(siebelId)
                .map(oldCustomer -> setValueIfRequired(customer, oldCustomer))
                .map(CustomerDto::new)
                .orElseThrow(ActiveCustomerNotFoundException::new);

    }

    public CustomerDto updateCustomerStatus(String  customerId, boolean operation) {
        log.atDebug()
                .addKeyValue("customerId", customerId)
                .addKeyValue("operation", operation)
                .log("Updating customer status");

        return customerRepository.findBySiebelId(customerId)
                .map(customer -> {
                    customer.setActive(operation);
                    return customerRepository.save(customer);
                })
                .map(CustomerDto::new)
                .orElseThrow(() -> new ActiveCustomerNotFoundException("Customer (%s) not found".formatted(customerId)));
    }

    private Customer setValueIfRequired(CustomerDto newValue, Customer customer) {

        log.atDebug()
                .addKeyValue("newValue", newValue)
                .log("Setting values for customer");

        setValueIfNotNull(customer::setSiebelId, newValue::getSiebelId);
        setValueIfNotNull(customer::setActive, newValue::isActive);
        setValueIfNotNull(customer::setFirstName, newValue::getFirstName);
        setValueIfNotNull(customer::setLastName, newValue::getLastName);
        setValueIfNotNull(customer::setEmail, newValue::getEmail);
        setValueIfNotNull(customer::setCustomerType, newValue::getCustomerType);
        setValueIfNotNull(customer::setCountryCode, newValue::getCountryCode);
        return customerRepository.save(customer);
    }

    private static <T> void setValueIfNotNull(Consumer<T> consumer, Supplier<T> supplier) {
        T value = supplier.get();
        if (!Objects.isNull(value)) {
            consumer.accept(value);
        }
    }


}
