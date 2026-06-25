package com.example.exo12.service;

import com.example.exo12.exception.InvalidTimeSlotException;
import com.example.exo12.exception.ReservationAlreadyCancelledException;
import com.example.exo12.exception.ReservationConflictException;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
import com.example.exo12.model.Room;
import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private final LocalDateTime start = LocalDateTime.of(2026, 6, 25, 10, 0);
    private final LocalDateTime end = LocalDateTime.of(2026, 6, 25, 11, 0);

    @Test
    void shouldCreateReservationWhenRoomExists() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Room A", 10)));
        when(reservationRepository.findConfirmedByRoomId(1L)).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> {
            Reservation r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        Reservation result = reservationService.createReservation(1L, "Alice", start, end);

        assertEquals(1L, result.getRoomId());
        assertEquals("Alice", result.getReservedBy());
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void shouldRejectWhenRoomDoesNotExist() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> reservationService.createReservation(99L, "Bob", start, end));

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldRejectWhenEndIsNotAfterStart() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Room B", 5)));

        LocalDateTime sameTime = LocalDateTime.of(2026, 6, 25, 14, 0);

        assertThrows(InvalidTimeSlotException.class,
                () -> reservationService.createReservation(1L, "Charlie", sameTime, sameTime));
    }

    @Test
    void shouldRejectWhenSlotOverlaps() {
        Room room = new Room(1L, "Room C", 8);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Reservation existing = new Reservation(
                1L, 1L, "David", start, end, ReservationStatus.CONFIRMED
        );
        when(reservationRepository.findConfirmedByRoomId(1L)).thenReturn(List.of(existing));

        LocalDateTime overlapStart = LocalDateTime.of(2026, 6, 25, 10, 30);
        LocalDateTime overlapEnd = LocalDateTime.of(2026, 6, 25, 11, 30);

        assertThrows(ReservationConflictException.class,
                () -> reservationService.createReservation(1L, "Eve", overlapStart, overlapEnd));
    }

    @Test
    void shouldCancelConfirmedReservation() {
        Reservation reservation = new Reservation(1L, 1L, "Alice", start, end, ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
    }

    @Test
    void shouldRejectCancelWhenAlreadyCancelled() {
        Reservation cancelled = new Reservation(2L, 1L, "Bob", start, end, ReservationStatus.CANCELLED);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(cancelled));

        assertThrows(ReservationAlreadyCancelledException.class,
                () -> reservationService.cancelReservation(2L));

        verify(reservationRepository, never()).save(any());
    }
}
