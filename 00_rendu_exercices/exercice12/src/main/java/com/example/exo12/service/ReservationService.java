package com.example.exo12.service;

import com.example.exo12.exception.InvalidTimeSlotException;
import com.example.exo12.exception.ReservationAlreadyCancelledException;
import com.example.exo12.exception.ReservationConflictException;
import com.example.exo12.exception.ReservationNotFoundException;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
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
        roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));

        if (!endTime.isAfter(startTime)) {
            throw new InvalidTimeSlotException();
        }

        for (Reservation existing : reservationRepository.findConfirmedByRoomId(roomId)) {
            if (overlaps(startTime, endTime, existing.getStartTime(), existing.getEndTime())) {
                throw new ReservationConflictException();
            }
        }

        Reservation reservation = new Reservation();
        reservation.setRoomId(roomId);
        reservation.setReservedBy(reservedBy);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException();
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    private boolean overlaps(LocalDateTime start1, LocalDateTime end1,
                             LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
