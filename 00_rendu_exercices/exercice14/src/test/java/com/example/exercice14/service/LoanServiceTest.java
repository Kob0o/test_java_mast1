package com.example.exercice14.service;

import com.example.exercice14.exception.ItemNotAvailableException;
import com.example.exercice14.exception.LoanNotFoundException;
import com.example.exercice14.exception.MemberSuspendedException;
import com.example.exercice14.model.Loan;
import com.example.exercice14.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private LoanService loanService;

    private final LocalDate borrowDate = LocalDate.of(2026, 3, 1);

    @Test
    void shouldCreateLoanWithDueDateIn21Days() {
        // Arrange
        when(loanRepository.countMajorLateReturnsInYear("MEMBER-001", 2026)).thenReturn(0);
        when(loanRepository.findActiveByItemId("BOOK-01")).thenReturn(Optional.empty());
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> {
            Loan loan = inv.getArgument(0);
            loan.setId(1L);
            return loan;
        });

        // Act
        Loan result = loanService.borrow("MEMBER-001", "BOOK-01", borrowDate);

        // Assert
        assertThat(result.getMemberId()).isEqualTo("MEMBER-001");
        assertThat(result.getItemId()).isEqualTo("BOOK-01");
        assertThat(result.getDueDate()).isEqualTo(borrowDate.plusDays(21));
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void shouldRejectLoanWhenItemAlreadyBorrowed() {
        // Arrange
        Loan activeLoan = new Loan(1L, "MEMBER-002", "BOOK-01", borrowDate, borrowDate.plusDays(21));
        when(loanRepository.countMajorLateReturnsInYear("MEMBER-001", 2026)).thenReturn(0);
        when(loanRepository.findActiveByItemId("BOOK-01")).thenReturn(Optional.of(activeLoan));

        // Act & Assert
        assertThatThrownBy(() -> loanService.borrow("MEMBER-001", "BOOK-01", borrowDate))
                .isInstanceOf(ItemNotAvailableException.class);

        verify(loanRepository, never()).save(any());
    }

    @Test
    void shouldRejectLoanWhenMemberIsSuspended() {
        // Arrange
        when(loanRepository.countMajorLateReturnsInYear("MEMBER-001", 2026)).thenReturn(3);

        // Act & Assert
        assertThatThrownBy(() -> loanService.borrow("MEMBER-001", "BOOK-01", borrowDate))
                .isInstanceOf(MemberSuspendedException.class);

        verify(loanRepository, never()).findActiveByItemId(anyString());
    }

    @Test
    void shouldReturnZeroPenaltyWhenReturnedOnTime() {
        // Arrange
        Loan loan = new Loan(1L, "MEMBER-001", "BOOK-01", borrowDate, borrowDate.plusDays(21));
        LocalDate returnDate = loan.getDueDate();

        // Act
        BigDecimal penalty = loanService.calculatePenalty(loan, returnDate);

        // Assert
        assertThat(penalty).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculatePenaltyForLateReturn() {
        // Arrange
        Loan loan = new Loan(1L, "MEMBER-001", "BOOK-01", borrowDate, borrowDate.plusDays(21));
        LocalDate returnDate = loan.getDueDate().plusDays(4);

        // Act
        BigDecimal penalty = loanService.calculatePenalty(loan, returnDate);

        // Assert
        assertThat(penalty).isEqualByComparingTo(new BigDecimal("0.60"));
    }

    @Test
    void shouldReturnLoanAndApplyPenalty() {
        // Arrange
        Loan loan = new Loan(1L, "MEMBER-001", "BOOK-01", borrowDate, borrowDate.plusDays(21));
        LocalDate returnDate = loan.getDueDate().plusDays(2);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Loan result = loanService.returnItem(1L, returnDate);

        // Assert
        assertThat(result.getReturnDate()).isEqualTo(returnDate);
        assertThat(result.isMajorLate()).isFalse();
        verify(loanRepository).save(loan);
    }

    @Test
    void shouldMarkReturnAsMajorLateWhenDelayIsAtLeast7Days() {
        // Arrange
        Loan loan = new Loan(1L, "MEMBER-001", "BOOK-01", borrowDate, borrowDate.plusDays(21));
        LocalDate returnDate = loan.getDueDate().plusDays(7);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Loan result = loanService.returnItem(1L, returnDate);

        // Assert
        assertThat(result.isMajorLate()).isTrue();
    }

    @Test
    void shouldThrowWhenReturningUnknownLoan() {
        // Arrange
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> loanService.returnItem(99L, LocalDate.now()))
                .isInstanceOf(LoanNotFoundException.class);
    }
}
