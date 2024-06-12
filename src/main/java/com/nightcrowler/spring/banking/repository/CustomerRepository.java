package com.nightcrowler.spring.banking.repository;

import com.nightcrowler.spring.banking.domain.Customer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends ListCrudRepository<Customer, Long> {
    Optional<Customer> findBySiebelIdAndActiveTrue(final String customerId);

    Optional<Customer> findBySiebelIdAndActive(final String customerId, final boolean active);

    boolean existsBySiebelId(final String siebelId);

    Optional<Customer> findBySiebelId(final String siebelId);
}
