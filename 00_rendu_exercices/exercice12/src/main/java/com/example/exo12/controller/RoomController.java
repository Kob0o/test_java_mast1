package com.example.exo12.controller;

import com.example.exo12.dto.CreateRoomRequest;
import com.example.exo12.dto.RoomResponse;
import com.example.exo12.model.Room;
import com.example.exo12.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
        Room created = roomService.createRoom(request.name(), request.capacity());
        RoomResponse response = RoomResponse.from(created);

        return ResponseEntity.created(URI.create("/api/rooms/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll() {
        List<RoomResponse> rooms = roomService.getAllRooms().stream()
                .map(RoomResponse::from)
                .toList();

        return ResponseEntity.ok(rooms);
    }
}
