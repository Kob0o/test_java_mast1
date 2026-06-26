package com.example.exercice13.service;

import com.example.exercice13.exception.AccountAlreadyExistsException;
import com.example.exercice13.exception.AccountNotFoundException;
import com.example.exercice13.exception.InsufficientFundsException;
import com.example.exercice13.exception.InvalidAmountException;
import com.example.exercice13.model.Account;
import com.example.exercice13.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccount_whenNumberIsNew() {
        // Arrange
        when(accountRepository.existsByNumber("ACC-001")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Account result = accountService.createAccount("ACC-001", "Alice");

        // Assert
        assertEquals("ACC-001", result.getNumber());
        assertEquals("Alice", result.getHolder());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldRejectCreation_whenNumberAlreadyExists() {
        // Arrange
        when(accountRepository.existsByNumber("ACC-001")).thenReturn(true);

        // Act & Assert
        assertThrows(AccountAlreadyExistsException.class,
                () -> accountService.createAccount("ACC-001", "Alice"));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldReturnAccount_whenItExists() {
        // Arrange
        Account existing = new Account("ACC-001", "Alice", BigDecimal.TEN);
        when(accountRepository.findByNumber("ACC-001")).thenReturn(Optional.of(existing));

        // Act
        Account result = accountService.getAccount("ACC-001");

        // Assert
        assertEquals(existing, result);
    }

    @Test
    void shouldThrow_whenAccountNotFound() {
        // Arrange
        when(accountRepository.findByNumber("ACC-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccount("ACC-999"));
    }

    @Test
    void shouldReturnAllAccounts() {
        // Arrange
        List<Account> accounts = List.of(
                new Account("ACC-001", "Alice", BigDecimal.ZERO),
                new Account("ACC-002", "Bob", BigDecimal.valueOf(50))
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountService.getAllAccounts();

        // Assert
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    void shouldDeposit_whenAmountIsValid() {
        // Arrange
        Account account = new Account("ACC-001", "Alice", BigDecimal.valueOf(100));
        when(accountRepository.findByNumber("ACC-001")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Account result = accountService.deposit("ACC-001", BigDecimal.valueOf(50));

        // Assert
        assertEquals(BigDecimal.valueOf(150), result.getBalance());
    }

    @Test
    void shouldRejectDeposit_whenAmountIsZero() {
        // Act & Assert
        assertThrows(InvalidAmountException.class,
                () -> accountService.deposit("ACC-001", BigDecimal.ZERO));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldRejectDeposit_whenAmountIsNegative() {
        // Act & Assert
        assertThrows(InvalidAmountException.class,
                () -> accountService.deposit("ACC-001", BigDecimal.valueOf(-5)));
    }

    @Test
    void shouldWithdraw_whenAmountIsValid() {
        // Arrange
        Account account = new Account("ACC-001", "Alice", BigDecimal.valueOf(200));
        when(accountRepository.findByNumber("ACC-001")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Account result = accountService.withdraw("ACC-001", BigDecimal.valueOf(50));

        // Assert
        assertEquals(BigDecimal.valueOf(150), result.getBalance());
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsZero() {
        // Act & Assert
        assertThrows(InvalidAmountException.class,
                () -> accountService.withdraw("ACC-001", BigDecimal.ZERO));
    }

    @Test
    void shouldRejectWithdraw_whenAmountIsNegative() {
        // Act & Assert
        assertThrows(InvalidAmountException.class,
                () -> accountService.withdraw("ACC-001", BigDecimal.valueOf(-1)));
    }

    @Test
    void shouldRejectWithdraw_whenInsufficientFunds() {
        // Arrange
        when(accountRepository.findByNumber("ACC-001"))
                .thenReturn(Optional.of(new Account("ACC-001", "Alice", BigDecimal.valueOf(30))));

        // Act & Assert
        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw("ACC-001", BigDecimal.valueOf(100)));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldTransfer_whenAmountIsValid() {
        // Arrange
        Account source = new Account("ACC-001", "Alice", BigDecimal.valueOf(500));
        Account target = new Account("ACC-002", "Bob", BigDecimal.ZERO);
        when(accountRepository.findByNumber("ACC-001")).thenReturn(Optional.of(source));
        when(accountRepository.findByNumber("ACC-002")).thenReturn(Optional.of(target));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        accountService.transfer("ACC-001", "ACC-002", BigDecimal.valueOf(150));

        // Assert
        assertEquals(BigDecimal.valueOf(350), source.getBalance());
        assertEquals(BigDecimal.valueOf(150), target.getBalance());
        verify(accountRepository).save(source);
        verify(accountRepository).save(target);
    }

    @Test
    void shouldRejectTransfer_whenAmountIsZero() {
        // Act & Assert
        assertThrows(InvalidAmountException.class,
                () -> accountService.transfer("ACC-001", "ACC-002", BigDecimal.ZERO));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldRejectTransfer_whenAmountIsNegative() {
        // Act & Assert
        assertThrows(InvalidAmountException.class,
                () -> accountService.transfer("ACC-001", "ACC-002", BigDecimal.valueOf(-10)));
    }

    @Test
    void shouldRejectTransfer_whenInsufficientFunds() {
        // Arrange
        Account source = new Account("ACC-001", "Alice", BigDecimal.valueOf(50));
        Account target = new Account("ACC-002", "Bob", BigDecimal.ZERO);
        when(accountRepository.findByNumber("ACC-001")).thenReturn(Optional.of(source));
        when(accountRepository.findByNumber("ACC-002")).thenReturn(Optional.of(target));

        // Act & Assert
        assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer("ACC-001", "ACC-002", BigDecimal.valueOf(100)));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldRejectTransfer_whenTargetAccountNotFound() {
        // Arrange
        when(accountRepository.findByNumber("ACC-001"))
                .thenReturn(Optional.of(new Account("ACC-001", "Alice", BigDecimal.valueOf(100))));
        when(accountRepository.findByNumber("ACC-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer("ACC-001", "ACC-999", BigDecimal.TEN));
    }

    @Test
    void shouldRejectTransfer_whenSourceAccountNotFound() {
        // Arrange
        when(accountRepository.findByNumber("ACC-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer("ACC-999", "ACC-002", BigDecimal.TEN));
        verify(accountRepository, never()).findByNumber("ACC-002");
    }
}
