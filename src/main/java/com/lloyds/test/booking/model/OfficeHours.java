package com.lloyds.test.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OfficeHours {
    private final LocalTime officeStartHours;
    private final LocalTime officeEndHours;

}
