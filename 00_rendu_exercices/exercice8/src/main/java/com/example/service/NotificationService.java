package com.example.service;

import com.example.model.Reservation;

public interface NotificationService {

    void sendConfirmation(Reservation reservation);
}
