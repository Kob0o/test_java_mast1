package com.example.exercice14.service;

import com.example.exercice14.exception.NotImplementedException;
import com.example.exercice14.model.Reservation;
import com.example.exercice14.repository.LoanRepository;
import com.example.exercice14.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LoanRepository loanRepository;

    public ReservationService(ReservationRepository reservationRepository,
            LoanRepository loanRepository) {
        this.reservationRepository = reservationRepository;
        this.loanRepository = loanRepository;
    }

    public Reservation reserve(String memberId, String itemId) {
        throw new NotImplementedException();
    }
}
