package com.lloyds.test.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Bookings {
    public String emp_id;
    @JsonFormat(pattern="HH:mm")
    public LocalTime start_time;
    @JsonFormat(pattern="HH:mm")
    public LocalTime end_time;
}
