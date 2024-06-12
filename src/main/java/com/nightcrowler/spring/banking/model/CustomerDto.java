package com.nightcrowler.spring.banking.model;


import com.nightcrowler.spring.banking.domain.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerDto {
    private boolean active;
    private String siebelId;
    private String firstName;
    private String lastName;
    private String email;
    private String countryCode;
    private Customer.CustomerType customerType;
    private List<AccountDto> accounts = new LinkedList<>();

    public CustomerDto(Customer customer) {
        this.active = customer.isActive();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.customerType = customer.getCustomerType();
        this.countryCode = customer.getCountryCode();
        this.siebelId = customer.getSiebelId();
        customer.getAccounts().forEach(account -> accounts.add(new AccountDto(account)));
    }
}
