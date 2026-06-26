package com.example.exercice14.service;

import com.example.exercice14.exception.NotImplementedException;
import com.example.exercice14.model.Loan;
import com.example.exercice14.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan borrow(String memberId, String itemId, LocalDate borrowDate) {
        throw new NotImplementedException();
    }

    public Loan returnItem(Long loanId, LocalDate returnDate) {
        throw new NotImplementedException();
    }

    public BigDecimal calculatePenalty(Loan loan, LocalDate returnDate) {
        throw new NotImplementedException();
    }
}
