package com.example.service;

import com.example.model.Reservation;
import com.example.model.ReservationResult;
import com.example.model.Room;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;

import java.util.List;

public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationService(RoomRepository roomRepository,
                              ReservationRepository reservationRepository,
                              NotificationService notificationService) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public ReservationResult reserve(Reservation reservation) {
        Room room = roomRepository.findByCode(reservation.getRoomCode()).orElse(null);

        if (room == null) {
            return new ReservationResult(false);
        }

        if (!reservation.getEndDate().isAfter(reservation.getStartDate())) {
            return new ReservationResult(false);
        }

        List<Reservation> existingReservations = reservationRepository.findByRoomCode(reservation.getRoomCode());

        if (reservation.getParticipantCount() > room.getMaxCapacity()) {
            return new ReservationResult(false);
        }

        for (Reservation existingReservation : existingReservations) {
            if (overlaps(reservation, existingReservation)) {
                return new ReservationResult(false);
            }
        }

        notificationService.sendConfirmation(reservation);
        return new ReservationResult(true);
    }

    private boolean overlaps(Reservation requested, Reservation existing) {
        return requested.getStartDate().isBefore(existing.getEndDate())
                && existing.getStartDate().isBefore(requested.getEndDate());
    }
}
