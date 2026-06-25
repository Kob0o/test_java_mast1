package com.example.exo12.repository;

import com.example.exo12.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findConfirmedByRoomId(Long roomId);

    void deleteAll();
}
