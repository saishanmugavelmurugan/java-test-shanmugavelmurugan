package com.lloyds.test.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BookingRequest {
    private String EmployeeId;
    private LocalDateTime submissionTime;
    private LocalDate meetingStartDate;
    private LocalTime meetingStartTime;
    private LocalDateTime meetingStartDateTime;
    private LocalDateTime meetingEndDateTime;
    private LocalTime meetingEndTime;
    private Long hour;

}
