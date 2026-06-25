package com.example.exo12.dto;

import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long roomId,
        String reservedBy,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoomId(),
                reservation.getReservedBy(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus()
        );
    }
}
