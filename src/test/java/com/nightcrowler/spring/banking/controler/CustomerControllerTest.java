package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.CustomerDto;
import com.nightcrowler.spring.banking.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@DisplayName("Customer Controller Test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;


    @Test
    @DisplayName("Test Get Customers")
    void testGetCustomersDetails() throws Exception {
        given(customerService.getAllCustomers()).willReturn(List.of(getCustomerDto()));
        mvc.perform(get("/api/v1/administration/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].siebelId").value("LONGYILMAZ"))
                .andExpect(jsonPath("$[0].firstName").value("Long"))
                .andExpect(jsonPath("$[0].lastName").value("Yilmaz"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].email").value("email.id"))
                .andExpect(jsonPath("$[0].accounts[0].accountNumber").value("ACC1"))
                .andExpect(jsonPath("$[0].accounts[0].accountBalance").value(1000))
                .andExpect(jsonPath("$[0].accounts[0].active").value(true))
                .andExpect(jsonPath("$[0].accounts[0].currencyCode").value("USD"));


    }

    @Test
    @DisplayName("Test Get Customer By Id")
    void testGetCustomerById() throws Exception {
        given(customerService.updateCustomerStatus("LONGYILMAZ", false)).willReturn(getCustomerDto());
        mvc.perform(get("/api/v1/administration/customers/LONGYILMAZ?active=false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siebelId").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.firstName").value("Long"))
                .andExpect(jsonPath("$.lastName").value("Yilmaz"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.email").value("email.id"))
                .andExpect(jsonPath("$.accounts[0].accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accounts[0].accountBalance").value(1000))
                .andExpect(jsonPath("$.accounts[0].active").value(true))
                .andExpect(jsonPath("$.accounts[0].currencyCode").value("USD"));

    }

    @Test
    @DisplayName("Test Add Customer")
    void testAddCustomer() throws Exception {
        given(customerService.createCustomer(any())).willReturn(getCustomerDto());
        mvc.perform(post("/api/v1/administration/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "siebelId": "LAXMIPRAKASH",
                          "firstName": "Long",
                          "lastName": "Yilmaz",
                          "email": "email.id",
                          "active": true
                        }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siebelId").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.firstName").value("Long"))
                .andExpect(jsonPath("$.lastName").value("Yilmaz"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.email").value("email.id"));
    }

    @Test
    @DisplayName("Test Update Customer")
    void testUpdateCustomer() throws Exception {
        given(customerService.updateCustomer(anyString(), any())).willReturn(getCustomerDto());
        mvc.perform(put("/api/v1/administration/customers/LONGYILMAZ/details")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "siebelId": "LONGYILMAZ",
                          "firstName": "Long",
                          "lastName": "Yilmaz",
                          "email": "email.id",
                          "active": true
                        }"""))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.siebelId").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.firstName").value("Long"))
                .andExpect(jsonPath("$.lastName").value("Yilmaz"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.email").value("email.id"));
    }

    @Test
    @DisplayName("Test Update Customer Status")
    void testUpdateCustomerStatus() throws Exception {
        var customer= getCustomerDto();
        customer.setActive(false);
        given(customerService.updateCustomerStatus(anyString(), anyBoolean())).willReturn(customer) ;
        mvc.perform(put("/api/v1/administration/customers/LONGYILMAZ/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "siebelId": "LONGYILMAZ",
                          "firstName": "Long",
                          "lastName": "Yilmaz",
                          "email": "email.id",
                          "active": true
                        }"""))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.siebelId").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.firstName").value("Long"))
                .andExpect(jsonPath("$.lastName").value("Yilmaz"))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.email").value("email.id"));
    }

    private CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setSiebelId("LONGYILMAZ");
        customerDto.setFirstName("Long");
        customerDto.setLastName("Yilmaz");
        customerDto.setEmail("email.id");
        customerDto.setActive(true);
        customerDto.setAccounts(List.of(getAccountDto()));
        return customerDto;
    }

    private AccountDto getAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("ACC1");
        accountDto.setCurrencyCode("USD");
        accountDto.setAccountBalance(BigDecimal.valueOf(1000));
        accountDto.setActive(true);
        return accountDto;
    }
}
