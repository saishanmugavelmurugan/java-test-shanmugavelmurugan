package com.lloyds.test.booking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingRequestExceptionHandlerTest {

    @Test
    public void whenInvalidOfficeHoursExceptionThrown_thenAssertionSucceeds() {
        Exception exception = assertThrows(InvalidOfficeHoursException.class, () -> {
           throw new InvalidOfficeHoursException("Invalid officeHours");
        });

        String expectedMessage = "Invalid officeHours";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void whenInvalidBookingRequestExceptionThrown_thenAssertionSucceeds() {
        Exception exception = assertThrows(InvalidBookingRequestException.class, () -> {
            throw new InvalidBookingRequestException("Invalid booking request");
        });

        String expectedMessage = "Invalid booking request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
