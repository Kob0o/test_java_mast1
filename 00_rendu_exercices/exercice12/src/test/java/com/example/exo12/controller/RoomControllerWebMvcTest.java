package com.example.exo12.controller;

import com.example.exo12.model.Room;
import com.example.exo12.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
class RoomControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void shouldReturnCreated_whenRoomIsValid() throws Exception {
        when(roomService.createRoom("Room A", 10))
                .thenReturn(new Room(1L, "Room A", 10));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Room A\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/rooms/1"))
                .andExpect(jsonPath("$.name").value("Room A"))
                .andExpect(jsonPath("$.capacity").value(10));

        verify(roomService).createRoom("Room A", 10);
    }

    @Test
    void shouldReturnBadRequest_whenRoomIsInvalid() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"capacity\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(roomService, never()).createRoom(anyString(), anyInt());
    }
}
