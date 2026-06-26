package com.example.exercice14.repository;

import com.example.exercice14.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findPendingByItemId(String itemId);

    int countPendingByItemId(String itemId);

    void deleteAll();
}
