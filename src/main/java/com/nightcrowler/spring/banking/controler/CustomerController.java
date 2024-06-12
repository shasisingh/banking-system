package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.model.CustomerDto;
import com.nightcrowler.spring.banking.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/administration/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getAllCustomers());

    }

    @GetMapping(path = "{siebelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable String siebelId,
            @RequestParam(name = "active", defaultValue = "true") boolean active
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.updateCustomerStatus(siebelId, active));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.createCustomer(customerDto));
    }

    @PutMapping(path = "{siebelId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable String siebelId, @RequestBody CustomerDto customerDto) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(customerService.updateCustomer(siebelId, customerDto));
    }

    @PutMapping(path = "{siebelId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> updateCustomerStatus(@PathVariable String siebelId, @RequestBody CustomerDto customerDto) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(customerService.updateCustomerStatus(siebelId, customerDto.isActive()));
    }


}
