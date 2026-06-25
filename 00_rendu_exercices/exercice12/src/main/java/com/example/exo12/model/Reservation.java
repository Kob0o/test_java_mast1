package com.example.exo12.model;

import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private Long roomId;
    private String reservedBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Long id, Long roomId, String reservedBy,
                       LocalDateTime startTime, LocalDateTime endTime,
                       ReservationStatus status) {
        this.id = id;
        this.roomId = roomId;
        this.reservedBy = reservedBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
