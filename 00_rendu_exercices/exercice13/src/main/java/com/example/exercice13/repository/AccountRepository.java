package com.example.exercice13.repository;

import com.example.exercice13.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findByNumber(String number);

    boolean existsByNumber(String number);

    List<Account> findAll();

    void deleteAll();
}
