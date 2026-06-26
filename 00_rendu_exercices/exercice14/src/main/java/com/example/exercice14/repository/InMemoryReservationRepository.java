package com.example.exercice14.repository;

import com.example.exercice14.model.Reservation;
import com.example.exercice14.model.ReservationStatus;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(sequence.incrementAndGet());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public List<Reservation> findPendingByItemId(String itemId) {
        return reservations.values().stream()
                .filter(r -> itemId.equals(r.getItemId()))
                .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                .sorted(Comparator.comparing(Reservation::getId))
                .toList();
    }

    @Override
    public int countPendingByItemId(String itemId) {
        return findPendingByItemId(itemId).size();
    }

    @Override
    public void deleteAll() {
        reservations.clear();
        sequence.set(0);
    }
}
