package com.lloyds.test.booking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingsTest {
    @Test
    void bookings_whenValidDataGiven_thenSuccess() {
        Bookings booking = new Bookings("EMP001", LocalTime.parse("10:17", DateTimeFormatter.ofPattern("HH:mm")),
                LocalTime.parse("12:30", DateTimeFormatter.ofPattern("HH:mm")));
        assertEquals("EMP001", booking.getEmp_id());
        assertEquals("10:17", booking.getStart_time().toString());
        assertEquals("12:30", booking.getEnd_time().toString());
    }

}
