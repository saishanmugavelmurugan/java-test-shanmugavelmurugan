package com.lloyds.test.booking.constants;

public class BookingConstants {
    public static String LINE_FEED_REG_PATTERN = "[\r\n]+";
    public static String START_END_HOURS_REG_PATTERN = "(\\d{4})\\s+(\\d{4})";
    public static String BOOKING_REQ_REG_PATTERN = "(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2})\\s+(\\d+)";
    public static String SUBMITTED_TIME_REG_PATTERN = "(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+(EMP\\d+)";

    public static String SUBMITTED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String MEETING_REQ_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static String EMP = "EMP";
    public static String HOURS_MIN = "HHmm";

}
