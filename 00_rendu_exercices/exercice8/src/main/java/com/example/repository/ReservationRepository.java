package com.example.repository;

import com.example.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findByRoomCode(String roomCode);
}
