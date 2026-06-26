package com.example.exercice14.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long id) {
        super("Loan not found: " + id);
    }
}
