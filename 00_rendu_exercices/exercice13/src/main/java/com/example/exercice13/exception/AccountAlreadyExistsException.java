package com.example.exercice13.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String number) {
        super("Account already exists: " + number);
    }
}
