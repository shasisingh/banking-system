package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.domain.Account;
import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.MoneyTransferDto;
import com.nightcrowler.spring.banking.service.AccountTransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@DisplayName("Transaction Controller Test")
class TransactionControllerTest {

    @MockBean
    private AccountTransactionService accountTransactionService;

    @Autowired
    private MockMvc mvc;


    @Test
    @DisplayName("Test Transfer Money")
    void testApiTransferMoney() throws Exception {
        given(accountTransactionService.performTransfer(any(MoneyTransferDto.class))).willReturn(getAccountDto());
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/banking/transactions/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "creditor": {
                                      "relationNumber": "LONGYILMAZ",
                                      "accountNumber": "ACC1",
                                      "accountBalance": 1000,
                                      "active": true,
                                      "currencyCode": "EUR",
                                      "accountType": "SAVINGS"
                                    },
                                    "debtor": {
                                      "relationNumber": "LONGYILMAZ",
                                      "accountNumber": "ACC2",
                                      "accountBalance": 1000,
                                      "active": true,
                                      "currencyCode": "EUR",
                                      "accountType": "SAVINGS"
                                    },
                                    "amount": 100
                                }"""
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationNumber").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accountBalance").value(1000))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.currencyCode").value("EUR"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));

    }

    @Test
    @DisplayName("Test Withdraw Money")
    void testApiWithdrawMoney() throws Exception {
        given(accountTransactionService.withdraw(any())).willReturn(getAccountDto());
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/banking/transactions/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "relationNumber": "LONGYILMAZ",
                                  "accountNumber": "ACC1",
                                  "accountBalance": 1000,
                                  "active": true,
                                  "currencyCode": "EUR",
                                  "accountType": "SAVINGS",
                                  "withdrawAmount": 100
                                }"""
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationNumber").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accountBalance").value(1000))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.currencyCode").value("EUR"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }

    @Test
    @DisplayName("Test Get Account Balance")
    void testApiGetAccountBalance() throws Exception {
        given(accountTransactionService.getAccountBalance(any())).willReturn(new BigDecimal("1000"));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/banking/transactions/accounts/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "relationNumber": "LONGYILMAZ",
                                  "accountNumber": "ACC1",
                                  "accountBalance": 1000,
                                  "active": true,
                                  "currencyCode": "EUR",
                                  "accountType": "SAVINGS"
                                }"""
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("1000"));
    }

    @Test
    @DisplayName("Test Deposit Money")
    void testApiDepositMoney() throws Exception {
        given(accountTransactionService.deposit(any())).willReturn(getAccountDto());
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/banking/transactions/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "relationNumber": "LONGYILMAZ",
                                  "accountNumber": "ACC1",
                                  "accountBalance": 1000,
                                  "active": true,
                                  "currencyCode": "EUR",
                                  "accountType": "SAVINGS",
                                  "depositAmount": 100
                                }"""
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relationNumber").value("LONGYILMAZ"))
                .andExpect(jsonPath("$.accountNumber").value("ACC1"))
                .andExpect(jsonPath("$.accountBalance").value(1000))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.currencyCode").value("EUR"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }

    private AccountDto getAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setRelationNumber("LONGYILMAZ");
        accountDto.setAccountNumber("ACC1");
        accountDto.setAccountBalance(new BigDecimal("1000"));
        accountDto.setActive(true);
        accountDto.setCurrencyCode("EUR");
        accountDto.setAccountType(Account.AccountType.SAVINGS);
        return accountDto;
    }
}
