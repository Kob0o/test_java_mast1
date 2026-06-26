package com.example.exercice13.service;

import com.example.exercice13.exception.NotImplementedException;
import com.example.exercice13.model.Account;
import com.example.exercice13.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String number, String holder) {
        throw new NotImplementedException();
    }

    public Account getAccount(String number) {
        throw new NotImplementedException();
    }

    public List<Account> getAllAccounts() {
        throw new NotImplementedException();
    }

    public Account deposit(String number, BigDecimal amount) {
        throw new NotImplementedException();
    }

    public Account withdraw(String number, BigDecimal amount) {
        throw new NotImplementedException();
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        throw new NotImplementedException();
    }
}
