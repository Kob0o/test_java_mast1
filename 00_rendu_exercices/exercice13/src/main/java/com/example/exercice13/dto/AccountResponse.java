package com.example.exercice13.dto;

import com.example.exercice13.model.Account;

import java.math.BigDecimal;

public record AccountResponse(String number, String holder, BigDecimal balance) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getNumber(),
                account.getHolder(),
                account.getBalance()
        );
    }
}
