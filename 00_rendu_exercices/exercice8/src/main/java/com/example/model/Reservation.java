package com.example.model;

import java.time.LocalDateTime;

public class Reservation {

    private final String userEmail;
    private final String roomCode;
    private final int participantCount;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Reservation(String userEmail, String roomCode, int participantCount,
                       LocalDateTime startDate, LocalDateTime endDate) {
        this.userEmail = userEmail;
        this.roomCode = roomCode;
        this.participantCount = participantCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
