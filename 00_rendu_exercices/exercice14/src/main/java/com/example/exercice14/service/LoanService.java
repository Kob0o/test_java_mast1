package com.example.exercice14.service;

import com.example.exercice14.exception.ItemNotAvailableException;
import com.example.exercice14.exception.LoanNotFoundException;
import com.example.exercice14.exception.MemberSuspendedException;
import com.example.exercice14.model.Loan;
import com.example.exercice14.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LoanService {

    private static final int LOAN_DURATION_DAYS = 21;
    private static final int MAJOR_LATE_THRESHOLD_DAYS = 7;
    private static final int SUSPENSION_THRESHOLD = 3;
    private static final BigDecimal PENALTY_PER_DAY = new BigDecimal("0.15");

    private final LoanRepository loanRepository;
    private final ReservationService reservationService;

    public LoanService(LoanRepository loanRepository, ReservationService reservationService) {
        this.loanRepository = loanRepository;
        this.reservationService = reservationService;
    }

    public Loan borrow(String memberId, String itemId, LocalDate borrowDate) {
        if (loanRepository.countMajorLateReturnsInYear(memberId, borrowDate.getYear()) >= SUSPENSION_THRESHOLD) {
            throw new MemberSuspendedException(memberId);
        }

        if (loanRepository.findActiveByItemId(itemId).isPresent()) {
            throw new ItemNotAvailableException(itemId);
        }

        Loan loan = new Loan();
        loan.setMemberId(memberId);
        loan.setItemId(itemId);
        loan.setBorrowDate(borrowDate);
        loan.setDueDate(borrowDate.plusDays(LOAN_DURATION_DAYS));

        return loanRepository.save(loan);
    }

    public Loan returnItem(Long loanId, LocalDate returnDate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.setReturnDate(returnDate);
        loan.setMajorLate(isMajorLate(loan.getDueDate(), returnDate));
        loanRepository.save(loan);

        reservationService.fulfillNextReservation(loan.getItemId(), returnDate);

        return loan;
    }

    public BigDecimal calculatePenalty(Loan loan, LocalDate returnDate) {
        if (!returnDate.isAfter(loan.getDueDate())) {
            return BigDecimal.ZERO;
        }

        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        return PENALTY_PER_DAY.multiply(BigDecimal.valueOf(daysLate));
    }

    private boolean isMajorLate(LocalDate dueDate, LocalDate returnDate) {
        if (!returnDate.isAfter(dueDate)) {
            return false;
        }
        return ChronoUnit.DAYS.between(dueDate, returnDate) >= MAJOR_LATE_THRESHOLD_DAYS;
    }
}
