package com.example.exo12.service;

import com.example.exo12.exception.NotImplementedException;
import com.example.exo12.model.Reservation;
import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository,
                            ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Long roomId, String reservedBy,
                                         LocalDateTime startTime, LocalDateTime endTime) {
        throw new NotImplementedException();
    }

    public Reservation getReservationById(Long id) {
        throw new NotImplementedException();
    }

    public Reservation cancelReservation(Long id) {
        throw new NotImplementedException();
    }
}
