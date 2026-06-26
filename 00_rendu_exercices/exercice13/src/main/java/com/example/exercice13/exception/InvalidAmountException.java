package com.example.exercice13.exception;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super("Amount must be strictly positive");
    }
}
