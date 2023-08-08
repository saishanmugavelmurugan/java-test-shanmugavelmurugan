package com.lloyds.test.booking.utils;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookingRequestUtils {
    /**
     * Check the given time is valid with start and end time.
     * @param start
     * @param end
     * @param giveTime
     * @return boolean
     */
    public boolean isValidTimeInverval(LocalTime start, LocalTime end, LocalTime giveTime){
        return giveTime.compareTo(start)>=0 && giveTime.compareTo(end)<0? true:false;
    }

    /**
     * Method used to get matcher for the given text is matched against the pattern given
     * @param text input string for match reg-pattern
     * @param regPattern  Pattern
     * @return Matcher
     */
    public Matcher getMatcher(String text, String regPattern) {
        Pattern pattern = Pattern.compile(regPattern);
        return pattern.matcher(text);
    }

    /**
     * Return localtime for given hours and minutes as String
     * @param hours   input String hours
     * @param minutes input String minutes
     * @return LocalTime
     */
    public  LocalTime getLocalTime(String hours, String minutes) {
        return LocalTime.of(Integer.valueOf(hours), Integer.valueOf(minutes));
    }
}
