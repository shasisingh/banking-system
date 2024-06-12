package com.nightcrowler.spring.banking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "siebel_id", length = 30, nullable = false)
    private String siebelId;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "customer_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerType customerType = CustomerType.INDIVIDUAL;

    @Column(name = "active")
    private boolean active;

    @Column(name = "country_code", nullable = false, length = 5)
    private String countryCode;

    @OneToMany(mappedBy = "customer")
    private List<Account> accounts = new LinkedList<>();

    public enum CustomerType {
        INDIVIDUAL, ORGANIZATION
    }


}
