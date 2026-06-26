package com.example.exercice14.model;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private String memberId;
    private String itemId;
    private LocalDate createdAt;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Long id, String memberId, String itemId,
                       LocalDate createdAt, ReservationStatus status) {
        this.id = id;
        this.memberId = memberId;
        this.itemId = itemId;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
