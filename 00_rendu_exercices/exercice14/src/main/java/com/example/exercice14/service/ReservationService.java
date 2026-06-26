package com.example.exercice14.service;

import com.example.exercice14.exception.ItemAvailableException;
import com.example.exercice14.exception.MemberSuspendedException;
import com.example.exercice14.model.Loan;
import com.example.exercice14.model.Reservation;
import com.example.exercice14.model.ReservationStatus;
import com.example.exercice14.repository.LoanRepository;
import com.example.exercice14.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationService {

    private static final int LOAN_DURATION_DAYS = 21;
    private static final int SUSPENSION_THRESHOLD = 3;

    private final ReservationRepository reservationRepository;
    private final LoanRepository loanRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              LoanRepository loanRepository) {
        this.reservationRepository = reservationRepository;
        this.loanRepository = loanRepository;
    }

    public Reservation reserve(String memberId, String itemId) {
        int year = LocalDate.now().getYear();
        if (loanRepository.countMajorLateReturnsInYear(memberId, year) >= SUSPENSION_THRESHOLD) {
            throw new MemberSuspendedException(memberId);
        }

        if (loanRepository.findActiveByItemId(itemId).isEmpty()) {
            throw new ItemAvailableException(itemId);
        }

        Reservation reservation = new Reservation();
        reservation.setMemberId(memberId);
        reservation.setItemId(itemId);
        reservation.setCreatedAt(LocalDate.now());
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }

    public void fulfillNextReservation(String itemId, LocalDate borrowDate) {
        var pending = reservationRepository.findPendingByItemId(itemId);
        if (pending.isEmpty()) {
            return;
        }

        Reservation first = pending.getFirst();
        first.setStatus(ReservationStatus.FULFILLED);
        reservationRepository.save(first);

        Loan loan = new Loan();
        loan.setMemberId(first.getMemberId());
        loan.setItemId(itemId);
        loan.setBorrowDate(borrowDate);
        loan.setDueDate(borrowDate.plusDays(LOAN_DURATION_DAYS));
        loanRepository.save(loan);
    }
}
