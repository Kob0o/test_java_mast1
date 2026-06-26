package com.example.exercice13.service;

import com.example.exercice13.exception.AccountAlreadyExistsException;
import com.example.exercice13.exception.AccountNotFoundException;
import com.example.exercice13.exception.InsufficientFundsException;
import com.example.exercice13.exception.InvalidAmountException;
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
        if (accountRepository.existsByNumber(number)) {
            throw new AccountAlreadyExistsException(number);
        }

        Account account = new Account();
        account.setNumber(number);
        account.setHolder(holder);
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    public Account getAccount(String number) {
        return accountRepository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account deposit(String number, BigDecimal amount) {
        validateAmount(amount);

        Account account = getAccount(number);
        account.setBalance(account.getBalance().add(amount));

        return accountRepository.save(account);
    }

    public Account withdraw(String number, BigDecimal amount) {
        validateAmount(amount);

        Account account = getAccount(number);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        validateAmount(amount);

        Account source = accountRepository.findByNumber(fromNumber)
                .orElseThrow(() -> new AccountNotFoundException(fromNumber));

        Account target = accountRepository.findByNumber(toNumber)
                .orElseThrow(() -> new AccountNotFoundException(toNumber));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(target);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }
}
