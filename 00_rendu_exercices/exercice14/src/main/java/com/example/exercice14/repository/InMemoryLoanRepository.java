package com.example.exercice14.repository;

import com.example.exercice14.model.Loan;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryLoanRepository implements LoanRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Loan> loans = new ConcurrentHashMap<>();

    @Override
    public Loan save(Loan loan) {
        if (loan.getId() == null) {
            loan.setId(sequence.incrementAndGet());
        }
        loans.put(loan.getId(), loan);
        return loan;
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return Optional.ofNullable(loans.get(id));
    }

    @Override
    public Optional<Loan> findActiveByItemId(String itemId) {
        return loans.values().stream()
                .filter(loan -> itemId.equals(loan.getItemId()))
                .filter(loan -> loan.getReturnDate() == null)
                .findFirst();
    }

    @Override
    public int countMajorLateReturnsInYear(String memberId, int year) {
        return (int) loans.values().stream()
                .filter(loan -> memberId.equals(loan.getMemberId()))
                .filter(Loan::isMajorLate)
                .filter(loan -> loan.getReturnDate() != null)
                .filter(loan -> loan.getReturnDate().getYear() == year)
                .count();
    }

    @Override
    public void deleteAll() {
        loans.clear();
        sequence.set(0);
    }
}
