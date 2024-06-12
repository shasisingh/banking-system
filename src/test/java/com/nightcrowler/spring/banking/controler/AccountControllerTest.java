package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.service.AccountService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class)
@DisplayName("Account Controller Test")
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;


    @Test
    @DisplayName("Test Get Accounts")
    void testGetAccounts() throws Exception {
        given(accountService.getAccountsBySiebelId("LONGYILMAZ")).willReturn(List.of(getAccountDto()));
        mvc.perform(get("/api/v1/administration/customers/LONGYILMAZ/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("ACC1"))
                .andExpect(jsonPath("$[0].accountType").value("SAVINGS"))
                .andExpect(jsonPath("$[0].accountBalance").value(1000))
                .andExpect(jsonPath("$[0].relationNumber").value("1234"));
    }

    @Test
    @DisplayName("Test Get Customer Account")
    void testGetCustomerAccount() throws Exception {
        given(accountService.getCustomerAccount("LONGYILMAZ", 1L)).willReturn(getAccountDto());
        mvc.perform(get("/api/v1/administration/customers/LONGYILMAZ/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.accountBalance").value(1000))
                .andExpect(jsonPath("$.relationNumber").value("1234"));
    }

    @Test
    @DisplayName("Test Add Account")
    void testAddAccount() throws Exception {
        given(accountService.addAccount(anyString(), any())).willReturn(getAccountDto());
        mvc.perform(post("/api/v1/administration/customers/LONGYILMAZ/accounts")
                .contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                  "accountNumber": "ACCT",
                                  "accountType": "SAVINGS",
                                  "accountBalance": 1000,
                                  "relationNumber": "1234"
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.accountBalance").value(1000))
                .andExpect(jsonPath("$.relationNumber").value("1234"));
    }

    @Test
    @DisplayName("Test Deactivate Account")
    void testDeactivateAccount() throws Exception {
        given(accountService.deactivateAccount(1L, "LONGYILMAZ", false)).willReturn(getAccountDto());
        mvc.perform(
                put("/api/v1/administration/customers/LONGYILMAZ/accounts/1/status?active=false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.accountBalance").value(1000))
                .andExpect(jsonPath("$.relationNumber").value("1234"));

    }


    private AccountDto getAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("ACC1");
        accountDto.setAccountType(Account.AccountType.SAVINGS);
        accountDto.setAccountBalance(new BigDecimal("1000.0"));
        accountDto.setRelationNumber("1234");
        return accountDto;
    }

}
