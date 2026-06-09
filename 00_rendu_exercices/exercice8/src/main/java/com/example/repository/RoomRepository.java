package com.example.repository;

import com.example.model.Room;

import java.util.Optional;

public interface RoomRepository {

    Optional<Room> findByCode(String code);
}
