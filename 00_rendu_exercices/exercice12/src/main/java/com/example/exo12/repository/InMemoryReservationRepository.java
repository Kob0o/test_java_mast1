package com.example.exo12.repository;

import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findConfirmedByRoomId(Long roomId) {
        return reservations.values().stream()
                .filter(r -> roomId.equals(r.getRoomId()))
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .toList();
    }

    @Override
    public void deleteAll() {
        reservations.clear();
        sequence.set(0);
    }
}
