package com.example.exo12.repository;

import com.example.exo12.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(sequence.incrementAndGet());
        }
        rooms.put(room.getId(), room);
        return room;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values()).stream()
                .sorted(Comparator.comparing(Room::getId))
                .toList();
    }

    @Override
    public void deleteAll() {
        rooms.clear();
        sequence.set(0);
    }
}
