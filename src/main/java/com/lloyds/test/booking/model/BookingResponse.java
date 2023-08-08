package com.lloyds.test.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class BookingResponse {
    public String date;
    public List<Bookings> bookings;
}
