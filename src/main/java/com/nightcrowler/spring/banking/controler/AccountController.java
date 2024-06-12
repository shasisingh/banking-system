package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.model.AccountDto;
import com.nightcrowler.spring.banking.service.AccountService;
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
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "{siebelId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountDto>> getAccounts(@PathVariable String siebelId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.getAccountsBySiebelId(siebelId));
    }

    @GetMapping(path = "{siebelId}/accounts/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getCustomerAccount(@PathVariable String  siebelId, @PathVariable Long accountId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.getCustomerAccount(siebelId, accountId));

    }

    @PostMapping(path = "{siebelId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> addAccount(@PathVariable String  siebelId, @RequestBody AccountDto account) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.addAccount(siebelId, account));
    }

    @PutMapping(path = "{siebelId}/accounts/{accountId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> deactivateAccount(
            @PathVariable String siebelId,
            @PathVariable Long accountId, @RequestParam(name = "active", defaultValue = "true") boolean active) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(accountService.deactivateAccount(accountId, siebelId, active));
    }


}
