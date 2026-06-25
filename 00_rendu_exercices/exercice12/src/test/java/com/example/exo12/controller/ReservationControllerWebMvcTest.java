package com.example.exo12.controller;

import com.example.exo12.exception.ReservationConflictException;
import com.example.exo12.exception.ReservationNotFoundException;
import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
import com.example.exo12.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    private final String validBody = """
            {
              "roomId": 1,
              "reservedBy": "Mary",
              "startTime": "2026-06-25T10:00:00",
              "endTime": "2026-06-25T11:00:00"
            }
            """;

    @Test
    void shouldReturnCreated_whenReservationIsValid() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 6, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 25, 11, 0);

        when(reservationService.createReservation(1L, "Mary", start, end))
                .thenReturn(new Reservation(1L, 1L, "Mary", start, end, ReservationStatus.CONFIRMED));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservedBy").value("Mary"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldReturnNotFound_whenReservationDoesNotExist() throws Exception {
        when(reservationService.getReservationById(99L))
                .thenThrow(new ReservationNotFoundException(99L));

        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(reservationService).getReservationById(99L);
    }

    @Test
    void shouldReturnConflict_whenSlotOverlaps() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 6, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 6, 25, 11, 0);

        when(reservationService.createReservation(1L, "Mary", start, end))
                .thenThrow(new ReservationConflictException());

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
