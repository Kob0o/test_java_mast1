package com.example.exercice13.controller;

import com.example.exercice13.dto.AccountResponse;
import com.example.exercice13.dto.AmountRequest;
import com.example.exercice13.dto.CreateAccountRequest;
import com.example.exercice13.dto.TransferRequest;
import com.example.exercice13.model.Account;
import com.example.exercice13.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        Account created = accountService.createAccount(request.number(), request.holder());
        AccountResponse response = AccountResponse.from(created);

        return ResponseEntity.created(URI.create("/api/accounts/" + response.number()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<AccountResponse> accounts = accountService.getAllAccounts().stream()
                .map(AccountResponse::from)
                .toList();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{number}")
    public ResponseEntity<AccountResponse> getByNumber(@PathVariable String number) {
        Account account = accountService.getAccount(number);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PostMapping("/{number}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request
    ) {
        Account updated = accountService.deposit(number, request.amount());
        return ResponseEntity.ok(AccountResponse.from(updated));
    }

    @PostMapping("/{number}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request
    ) {
        Account updated = accountService.withdraw(number, request.amount());
        return ResponseEntity.ok(AccountResponse.from(updated));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        accountService.transfer(request.fromNumber(), request.toNumber(), request.amount());
        return ResponseEntity.ok().build();
    }
}
