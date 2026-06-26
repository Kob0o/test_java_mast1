package com.example.exercice14.bdd;

import com.example.exercice14.model.Loan;
import com.example.exercice14.model.Reservation;
import com.example.exercice14.model.ReservationStatus;
import com.example.exercice14.repository.LoanRepository;
import com.example.exercice14.repository.ReservationRepository;
import com.example.exercice14.service.LoanService;
import com.example.exercice14.service.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationStepDefinitions {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Exception lastError;
    private Reservation lastReservation;

    @Given("no loan or reservation exists")
    public void noLoanOrReservationExists() {
        loanRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Given("item {string} is borrowed by member {string}")
    public void itemIsBorrowed(String itemId, String memberId) {
        Loan loan = new Loan();
        loan.setMemberId(memberId);
        loan.setItemId(itemId);
        loan.setBorrowDate(LocalDate.of(2026, 3, 1));
        loan.setDueDate(LocalDate.of(2026, 3, 22));
        loanRepository.save(loan);
    }

    @Given("member {string} is suspended")
    public void memberIsSuspended(String memberId) {
        loanRepository.deleteAll();
        reservationRepository.deleteAll();

        int currentYear = LocalDate.now().getYear();

        for (int i = 1; i <= 3; i++) {
            Loan lateReturn = new Loan();
            lateReturn.setMemberId(memberId);
            lateReturn.setItemId("BOOK-LATE-" + i);
            lateReturn.setBorrowDate(LocalDate.of(currentYear, 1, 1));
            lateReturn.setDueDate(LocalDate.of(currentYear, 1, 22));
            lateReturn.setReturnDate(LocalDate.of(currentYear, 2, i));
            lateReturn.setMajorLate(true);
            loanRepository.save(lateReturn);
        }
    }

    @Given("member {string} has reserved item {string}")
    public void memberHasReserved(String memberId, String itemId) {
        Reservation reservation = new Reservation();
        reservation.setMemberId(memberId);
        reservation.setItemId(itemId);
        reservation.setCreatedAt(LocalDate.of(2026, 3, 5));
        reservation.setStatus(ReservationStatus.PENDING);
        reservationRepository.save(reservation);
    }

    @When("member {string} reserves item {string}")
    public void memberReservesItem(String memberId, String itemId) {
        lastError = null;
        lastReservation = null;

        try {
            lastReservation = reservationService.reserve(memberId, itemId);
        } catch (Exception e) {
            lastError = e;
        }
    }

    @When("member {string} returns item {string}")
    public void memberReturnsItem(String memberId, String itemId) {
        Loan loan = loanRepository.findActiveByItemId(itemId)
                .orElseThrow(() -> new IllegalStateException("No active loan for " + itemId));

        assertThat(loan.getMemberId()).isEqualTo(memberId);
        loanService.returnItem(loan.getId(), LocalDate.of(2026, 3, 15));
    }

    @Then("the reservation is accepted")
    public void reservationIsAccepted() {
        assertThat(lastError).isNull();
        assertThat(lastReservation).isNotNull();
    }

    @Then("the reservation is rejected")
    public void reservationIsRejected() {
        assertThat(lastError).isNotNull();
    }

    @Then("item {string} has {int} pending reservations")
    public void itemHasPendingReservations(String itemId, int count) {
        assertThat(reservationRepository.countPendingByItemId(itemId)).isEqualTo(count);
    }

    @Then("member {string} has borrowed item {string}")
    public void memberHasBorrowedItem(String memberId, String itemId) {
        assertThat(loanRepository.findActiveByItemId(itemId))
                .isPresent()
                .get()
                .extracting(Loan::getMemberId)
                .isEqualTo(memberId);
    }
}
