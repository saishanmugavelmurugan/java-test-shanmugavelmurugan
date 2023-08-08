package com.lloyds.test.booking.service;

import com.lloyds.test.booking.exception.InvalidBookingRequestException;
import com.lloyds.test.booking.exception.InvalidOfficeHoursException;
import com.lloyds.test.booking.model.BookingRequest;
import com.lloyds.test.booking.model.BookingResponse;
import com.lloyds.test.booking.model.OfficeHours;
import com.lloyds.test.booking.utils.BookingRequestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.lloyds.test.booking.constants.BookingConstants.START_END_HOURS_REG_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookingRequestServiceTest {
    BookingRequestServiceImpl bookingRequestService  =new BookingRequestServiceImpl(new BookingRequestUtils());

    @Test
    public void processBookingRequestText_WhenValidBookingReqGiven_ThenReturnMeetingRequest(){
        String text="0800 1730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:10 2\n" +
                "2020-01-18 12:34:51 EMP002\n" +
                "2020-01-21 08:00 1";
        List<BookingResponse> bookingResponse = this.bookingRequestService.processBookingRequestText(text);
        assertEquals(1,bookingResponse.size());
        assertEquals(2,bookingResponse.get(0).getBookings().size());

    }

    @Test
    public void processBookingRequestText_WhenValidBookingReqWithMultipleDatesGiven_ThenReturnMeetingRequest(){
        String text="0800 1730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:10 2\n" +
                "2020-01-18 12:34:51 EMP003\n" +
                "2020-01-21 08:00 1\n" +
                "2020-01-18 12:34:53 EMP004\n" +
                "2020-01-21 11:00 1\n" +
                "2020-01-18 12:34:55 EMP005\n" +
                "2020-01-21 12:00 1\n" +
                "2020-01-18 10:17:09 EMP006\n" +
                "2020-01-21 12:00 2\n" +
                "2020-01-18 10:17:07 EMP007\n" +
                "2020-01-21 14:00 2\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-22 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-22 09:10 2\n" +
                "2020-01-18 12:34:51 EMP003\n" +
                "2020-01-22 08:00 1\n" +
                "2020-01-18 12:34:53 EMP004\n" +
                "2020-01-22 11:00 1\n" +
                "2020-01-18 12:34:55 EMP005\n" +
                "2020-01-22 12:00 1\n" +
                "2020-01-18 10:17:09 EMP006\n" +
                "2020-01-22 12:00 2\n" +
                "2020-01-18 10:17:07 EMP007\n" +
                "2020-01-22 14:00 2\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-23 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-23 09:10 2\n" +
                "2020-01-18 12:34:51 EMP003\n" +
                "2020-01-23 08:00 1\n" +
                "2020-01-18 12:34:51 EMP004\n" +
                "2020-01-22 11:00 1\n" +
                "2020-01-18 12:34:55 EMP005\n" +
                "2020-01-24 12:00 1\n" +
                "2020-01-18 10:17:09 EMP006\n" +
                "2020-01-25 12:00 2\n" +
                "2020-01-18 10:17:07 EMP007\n" +
                "2020-01-26 14:00 2";
        List<BookingResponse> bookingResponse = this.bookingRequestService.processBookingRequestText(text);
        assertEquals(6,bookingResponse.size());
    }


    @Test
    public void processBookingRequestText_WhenInValidBookingReqGiven_ThenThrowInvalidOfficeHoursException(){
        String text="0800 1730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:102";

        Exception exception = assertThrows(
                InvalidBookingRequestException.class,
                () -> bookingRequestService.processBookingRequestText(text));


        assertEquals("Invalid format of booking request received:2020-01-18 12:34:56 EMP002::2020-01-21 09:102", exception.getMessage());
    }
    @Test
    public void processBookingRequestText_WhenInValidOfficeHoursGiven_ThenThrowInvalidBookingRequestException(){
        String text="08001730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:10 2\n" +
                "2020-01-21 08:001";

        Exception exception = assertThrows(
                InvalidOfficeHoursException.class,
                () -> bookingRequestService.processBookingRequestText(text));


        assertEquals("Invalid office hours pattern (or) unable parse the office hours from the text:08001730", exception.getMessage());
    }


    @Test
    public void parseBookingRequest_WhenInValidBookingRequestGiven_thenReturnSuccess() throws Exception {
        List<String> lineList = new ArrayList<>();
        lineList.add("0800 1730");
        lineList.add("2020-01-18 10:17:06 EMP001");
        lineList.add("2020-01-21 09:00 2");
        List<BookingRequest> bookingRequestList = new ArrayList<>();
        LocalDateTime requestStartTime = LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime requestEndTime = LocalDateTime.parse("2020-01-21 09:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plusHours(Long.valueOf(2));
        bookingRequestList.add(new BookingRequest("EMP001", LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ,requestStartTime.toLocalDate(),requestStartTime.toLocalTime(),requestStartTime,requestEndTime,requestEndTime.toLocalTime(),Long.valueOf(2)));

        Method m = BookingRequestServiceImpl.class.getDeclaredMethod("parseBookingRequest",List.class);
        m.setAccessible(true);
        BookingRequestServiceImpl b =new BookingRequestServiceImpl(new BookingRequestUtils());
        List<BookingRequest> bookingRequestListActual  = (List<BookingRequest>) m.invoke(b,lineList);
        Assert.assertEquals(bookingRequestList.size(),bookingRequestListActual.size());

    }
    @Test
    public void parseBookingRequest_WhenValidBookingRequestGiven_thenReturnFailure() throws NoSuchMethodException {
        List<String> lineList = new ArrayList<>();
        lineList.add("0800 1730");
        lineList.add("2020-01-1810:17:06 EMP001");
        lineList.add("2020-01-21 09:00 2");
        List<BookingRequest> bookingRequestList = new ArrayList<>();
        LocalDateTime requestStartTime = LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime requestEndTime = LocalDateTime.parse("2020-01-21 09:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plusHours(Long.valueOf(2));
        bookingRequestList.add(new BookingRequest("EMP001", LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                , requestStartTime.toLocalDate(), requestStartTime.toLocalTime(), requestStartTime, requestEndTime, requestEndTime.toLocalTime(), Long.valueOf(2)));

        Method m = BookingRequestServiceImpl.class.getDeclaredMethod("parseBookingRequest", List.class);
        m.setAccessible(true);
        BookingRequestServiceImpl b = new BookingRequestServiceImpl(new BookingRequestUtils());
        // List<BookingRequest> bookingRequestListActual  = (List<BookingRequest>) m.invoke(b,lineList);
        Exception e = assertThrows(Exception.class, () -> m.invoke(b, lineList));
        assertEquals(InvalidBookingRequestException.class, e.getCause().getClass());

    }

    @Test
    public void parseOfficeHours_WhenValidOfficeHoursGiven_thenReturnSuccess() throws Exception {
        List<String> lineList = new ArrayList<>();
        lineList.add("0800 1730");
        lineList.add("2020-01-18 10:17:06 EMP001");
        lineList.add("2020-01-21 09:00 2");
        List<BookingRequest> bookingRequestList = new ArrayList<>();
        LocalDateTime requestStartTime = LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime requestEndTime = LocalDateTime.parse("2020-01-21 09:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plusHours(Long.valueOf(2));
        bookingRequestList.add(new BookingRequest("EMP001", LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ,requestStartTime.toLocalDate(),requestStartTime.toLocalTime(),requestStartTime,requestEndTime,requestEndTime.toLocalTime(),Long.valueOf(2)));

        Method m = BookingRequestServiceImpl.class.getDeclaredMethod("parseOfficeHours",String.class,String.class);
        m.setAccessible(true);
        BookingRequestServiceImpl b =new BookingRequestServiceImpl(new BookingRequestUtils());
        OfficeHours officeHours  = (OfficeHours) m.invoke(b,"0800 1730",START_END_HOURS_REG_PATTERN);
        Assert.assertNotNull(officeHours);

    }
    @Test
    public void parseBookingRequest_WhenInValidOfficeHoursGiven_thenReturnFailure() throws NoSuchMethodException {
        List<String> lineList = new ArrayList<>();
        lineList.add("08001730");
        lineList.add("2020-01-1810:17:06 EMP001");
        lineList.add("2020-01-21 09:00 2");
        List<BookingRequest> bookingRequestList = new ArrayList<>();
        LocalDateTime requestStartTime = LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime requestEndTime = LocalDateTime.parse("2020-01-21 09:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plusHours(Long.valueOf(2));
        bookingRequestList.add(new BookingRequest("EMP001", LocalDateTime.parse("2020-01-18 10:17:06", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                , requestStartTime.toLocalDate(), requestStartTime.toLocalTime(), requestStartTime, requestEndTime, requestEndTime.toLocalTime(), Long.valueOf(2)));

        Method m = BookingRequestServiceImpl.class.getDeclaredMethod("parseOfficeHours", String.class,String.class);
        m.setAccessible(true);
        BookingRequestServiceImpl b = new BookingRequestServiceImpl(new BookingRequestUtils());
        // List<BookingRequest> bookingRequestListActual  = (List<BookingRequest>) m.invoke(b,lineList);
        Exception e = assertThrows(Exception.class, () -> m.invoke(b, "08001730",START_END_HOURS_REG_PATTERN));
        assertEquals(InvalidOfficeHoursException.class, e.getCause().getClass());

    }
}
