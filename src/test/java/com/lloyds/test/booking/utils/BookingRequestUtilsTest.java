package com.lloyds.test.booking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.regex.Matcher;

import static com.lloyds.test.booking.constants.BookingConstants.START_END_HOURS_REG_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingRequestUtilsTest {

    @Autowired
    BookingRequestUtils bookingRequestUtils;


    @Test
    public void isValidTimeInverval_WhenValidTimeGiven_ThenReturnFailure() throws Exception {
        //Given
        LocalTime startTime = LocalTime.of(9,0);
        LocalTime endTime = LocalTime.of(10,0);
        LocalTime givenTime = LocalTime.of(11,0);
        //When
        boolean status = bookingRequestUtils.isValidTimeInverval(startTime,endTime,givenTime);

        //Then

        assertFalse(status);
    }
    @Test
    public void isValidTimeInverval_WhenTimeGivenIsWithInterval_ThenReturnSuccess() throws Exception {
        //Given
        LocalTime startTime = LocalTime.of(9,0);
        LocalTime endTime = LocalTime.of(10,0);
        LocalTime givenTime = LocalTime.of(9,30);
        //When
        boolean status = bookingRequestUtils.isValidTimeInverval(startTime,endTime,givenTime);

        //Then

        assertTrue(status);
    }


    @Test
    public void getMatcher_WhenValidTextGiven_ThenReturnSuccess() throws Exception {
        //Given
        String text = "0930 1000";
        //When
        Matcher matcher = bookingRequestUtils.getMatcher(text,START_END_HOURS_REG_PATTERN);
        //Then
        assertTrue(matcher.find());
    }
    @Test
    public void getMatcher_WhenValidOfficeHoursGiven_ThenReturnfailure() throws Exception {
        //Given
        String pattern ="";
        String text = "09301000";
        //When
        Matcher matcher = bookingRequestUtils.getMatcher(text,START_END_HOURS_REG_PATTERN);
        //Then
        assertFalse(matcher.find());
    }

}
