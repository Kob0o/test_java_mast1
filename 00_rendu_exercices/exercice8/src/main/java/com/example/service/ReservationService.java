package com.example.service;

import com.example.model.Reservation;
import com.example.model.ReservationResult;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
