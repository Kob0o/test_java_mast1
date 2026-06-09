package com.example;

import com.example.model.Reservation;
import com.example.model.ReservationResult;
import com.example.model.Room;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.example.service.NotificationService;
import com.example.service.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationSteps {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private ReservationService reservationService;
    private final List<Reservation> existingReservations = new ArrayList<>();
    private String userEmail;
    private ReservationResult reservationResult;

    @Given("a room {string} named {string} with capacity {int}")
    public void aRoomNamedWithCapacity(String code, String name, int capacity) {
        initMocks();

        Room room = new Room(code, name, capacity);
        when(roomRepository.findByCode(code)).thenReturn(Optional.of(room));
    }

    @Given("no room exists with code {string}")
    public void noRoomExistsWithCode(String code) {
        initMocks();

        when(roomRepository.findByCode(code)).thenReturn(Optional.empty());
    }

    @Given("an existing reservation from {string} to {string} for room {string}")
    public void anExistingReservationFromToForRoom(String startDate, String endDate, String roomCode) {
        existingReservations.add(new Reservation(
                "existing@example.com",
                roomCode,
                1,
                parseDateTime(startDate),
                parseDateTime(endDate)
        ));
    }

    @Given("a user with email {string}")
    public void aUserWithEmail(String email) {
        userEmail = email;
    }

    @When("I reserve room {string} for {int} participants from {string} to {string}")
    public void iReserveRoomForParticipantsFromTo(String roomCode, int participantCount,
                                                  String startDate, String endDate) {
        Reservation reservation = new Reservation(
                userEmail,
                roomCode,
                participantCount,
                parseDateTime(startDate),
                parseDateTime(endDate)
        );
        reservationResult = reservationService.reserve(reservation);
    }

    @Then("the reservation should be accepted")
    public void theReservationShouldBeAccepted() {
        assertNotNull(reservationResult);
        assertTrue(reservationResult.isAccepted());
    }

    @Then("the reservation should be refused")
    public void theReservationShouldBeRefused() {
        assertNotNull(reservationResult);
        assertFalse(reservationResult.isAccepted());
    }

    @Then("a confirmation notification should be sent to {string}")
    public void aConfirmationNotificationShouldBeSentTo(String email) {
        verify(notificationService).sendConfirmation(argThat(reservation ->
                email.equals(reservation.getUserEmail())
        ));
    }

    @Then("no confirmation notification should be sent")
    public void noConfirmationNotificationShouldBeSent() {
        verify(notificationService, never()).sendConfirmation(any(Reservation.class));
    }

    @Then("the room repository should have been queried for {string}")
    public void theRoomRepositoryShouldHaveBeenQueriedFor(String code) {
        verify(roomRepository).findByCode(code);
    }

    @Then("the reservation repository should have been queried for room {string}")
    public void theReservationRepositoryShouldHaveBeenQueriedForRoom(String roomCode) {
        verify(reservationRepository).findByRoomCode(roomCode);
    }

    private void initMocks() {
        roomRepository = mock(RoomRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        reservationService = new ReservationService(roomRepository, reservationRepository, notificationService);
        existingReservations.clear();

        when(reservationRepository.findByRoomCode(anyString())).thenAnswer(invocation ->
                existingReservations.stream()
                        .filter(reservation -> reservation.getRoomCode().equals(invocation.getArgument(0)))
                        .toList()
        );
    }

    private LocalDateTime parseDateTime(String value) {
        return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
    }
}
