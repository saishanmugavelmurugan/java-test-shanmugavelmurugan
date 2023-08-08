package com.lloyds.test.booking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfficeHoursTest {
    @Test
    void officeHours_whenValidDataGiven_thenSuccess() {
        OfficeHours officeHours = new OfficeHours(LocalTime.parse("10:17", DateTimeFormatter.ofPattern("HH:mm")),
                LocalTime.parse("12:30", DateTimeFormatter.ofPattern("HH:mm")));

        assertEquals("10:17", officeHours.getOfficeStartHours().toString());
        assertEquals("12:30", officeHours.getOfficeEndHours().toString());
    }
}
