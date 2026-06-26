package com.example.exercice14.repository;

import com.example.exercice14.model.Loan;

import java.util.Optional;

public interface LoanRepository {

    Loan save(Loan loan);

    Optional<Loan> findById(Long id);

    Optional<Loan> findActiveByItemId(String itemId);

    int countMajorLateReturnsInYear(String memberId, int year);
}
