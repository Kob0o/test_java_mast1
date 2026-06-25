package com.example.exo12.repository;

import com.example.exo12.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    void deleteAll();
}
