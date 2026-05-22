package com.example.exercice4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    void shouldAcceptPassword1() {
        assertTrue(passwordValidator.isValid("Password1!"));
        assertEquals("Password is valid", passwordValidator.getErrorMessage("Password1!"));
    }

    @Test
    void shouldAcceptAdmin2024() {
        assertTrue(passwordValidator.isValid("Admin2024@"));
        assertEquals("Password is valid", passwordValidator.getErrorMessage("Admin2024@"));
    }

    @Test
    void shouldRejectShortPassword() {
        assertFalse(passwordValidator.isValid("short1!"));
        assertEquals(
                "Password must contain at least 8 characters",
                passwordValidator.getErrorMessage("short1!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutLowercase() {
        assertFalse(passwordValidator.isValid("PASSWORD1!"));
        assertEquals(
                "Password must contain at least one lowercase letter",
                passwordValidator.getErrorMessage("PASSWORD1!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() {
        assertFalse(passwordValidator.isValid("password1!"));
        assertEquals(
                "Password must contain at least one uppercase letter",
                passwordValidator.getErrorMessage("password1!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutDigit() {
        assertFalse(passwordValidator.isValid("Password!"));
        assertEquals(
                "Password must contain at least one digit",
                passwordValidator.getErrorMessage("Password!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutSpecialCharacter() {
        assertFalse(passwordValidator.isValid("Password1"));
        assertEquals(
                "Password must contain at least one special character",
                passwordValidator.getErrorMessage("Password1")
        );
    }

    @Test
    void shouldRejectNullPassword() {
        assertFalse(passwordValidator.isValid(null));
        assertEquals("Password must not be null", passwordValidator.getErrorMessage(null));
    }

    @ParameterizedTest(name = "password={0} => valid={1}")
    @CsvSource(nullValues = "null", value = {
            "Password1!, true",
            "Admin2024@, true",
            "short1!, false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!, false",
            "Password1, false",
            "null, false"
    })
    void shouldValidatePasswordAccordingToRules(String password, boolean expectedValid) {
        assertEquals(expectedValid, passwordValidator.isValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@"})
    void shouldReturnTrueForValidPasswords(String password) {
        assertTrue(passwordValidator.isValid(password));
    }

    @ParameterizedTest(name = "password={0} => message={1}")
    @MethodSource("passwordAndExpectedMessages")
    void shouldReturnExpectedErrorMessage(String password, String expectedMessage) {
        assertEquals(expectedMessage, passwordValidator.getErrorMessage(password));
    }

    static Stream<Arguments> passwordAndExpectedMessages() {
        return Stream.of(
                Arguments.of("Password1!", "Password is valid"),
                Arguments.of("Admin2024@", "Password is valid"),
                Arguments.of(null, "Password must not be null"),
                Arguments.of("short1!", "Password must contain at least 8 characters"),
                Arguments.of("PASSWORD1!", "Password must contain at least one lowercase letter"),
                Arguments.of("password1!", "Password must contain at least one uppercase letter"),
                Arguments.of("Password!", "Password must contain at least one digit"),
                Arguments.of("Password1", "Password must contain at least one special character")
        );
    }


    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullOrEmptyPassword(String password) {
        assertFalse(passwordValidator.isValid(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnExpectedMessageForNullOrEmptyPassword(String password) {
        String expectedMessage = password == null
                ? "Password must not be null"
                : "Password must contain at least 8 characters";
        assertEquals(expectedMessage, passwordValidator.getErrorMessage(password));
    }
}
