package com.example.exo12.service;

import com.example.exo12.model.Room;
import com.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(String name, int capacity) {
        Room room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
