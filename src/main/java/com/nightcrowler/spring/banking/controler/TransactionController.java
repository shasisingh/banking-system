package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.model.AccountOperationDto;
import com.nightcrowler.spring.banking.model.MoneyTransferDto;
import com.nightcrowler.spring.banking.service.AccountTransactionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/banking/transactions")
public class TransactionController {

    private final AccountTransactionService accountTransactionService;

    public TransactionController(AccountTransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }

    @PostMapping(path = "accounts/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> transfer(@RequestBody MoneyTransferDto transferDTO) {
        return ResponseEntity
                .ok(accountTransactionService.performTransfer(transferDTO)
                );

    }

    @PostMapping(path = "accounts/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> withdraw(@RequestBody AccountOperationDto withdrawDto) {
        return ResponseEntity.ok(accountTransactionService.withdraw(withdrawDto));
    }

    @PostMapping(path = "accounts/deposit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> deposit(@RequestBody AccountOperationDto depositDto) {
        return ResponseEntity.ok(accountTransactionService.deposit(depositDto));
    }

    @GetMapping(path = "accounts/balance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAccountBalance(@RequestBody AccountDto accountNumber) {
        var account = accountTransactionService.getAccountBalance(accountNumber);
        return ResponseEntity.ok(account.toEngineeringString());
    }
}
