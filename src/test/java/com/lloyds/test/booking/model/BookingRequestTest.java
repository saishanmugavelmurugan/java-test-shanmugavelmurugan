package com.lloyds.test.booking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingRequestTest {

    @Test
    void BookingRequest_whenValidDataGiven_thenSuccess() {
        LocalDateTime requestStartTime = LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime requestEndTime = LocalDateTime.parse("2020-01-21 09:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plusHours(Long.valueOf(2));
        BookingRequest bookingRequest = new BookingRequest("EMP001",
                LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ), requestStartTime.toLocalDate(), requestStartTime.toLocalTime(), requestStartTime, requestEndTime, requestEndTime.toLocalTime(), Long.valueOf(2));;
        assertEquals("EMP001", bookingRequest.getEmployeeId());
        assertEquals("2020-01-18T10:17:06", bookingRequest.getSubmissionTime().toString());

    }
}
