package com.lloyds.test.booking.api;

import com.lloyds.test.booking.exception.InvalidOfficeHoursException;
import com.lloyds.test.booking.model.BookingResponse;
import com.lloyds.test.booking.model.Bookings;
import com.lloyds.test.booking.service.BookingRequestService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BookingRequestController.class)
public class BookingRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    public BookingRequestService bookingRequestService;

    @Test
    public void bookingRequestAsText_whenValidTextPosted_thenReturnResponseCode_OK() throws Exception {
        // Given
        String text="0800 1730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:10 2\n" +
                "2020-01-18 12:34:51 EMP002\n" +
                "2020-01-21 08:00 1";
        List<BookingResponse> bookingResponseList =new ArrayList<>();
        List<Bookings> bookingsList = new ArrayList<>();
        bookingsList.add(new Bookings("EMP001", LocalTime.of(9,00),LocalTime.of(10,00)));
        bookingResponseList.add(new BookingResponse("06-08-2023",bookingsList));
        when(bookingRequestService.processBookingRequestText(anyString()))
                .thenReturn(bookingResponseList);
        String excepted = "[{\"date\":\"06-08-2023\",\"bookings\":[{\"emp_id\":\"EMP001\",\"start_time\":\"09:00\",\"end_time\":\"10:00\"}]}]";

        // When
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/booking/text")
                        .contentType("text/plain")
                        .content(text))
                .andExpect(status().isOk())
                .andReturn();
        //List<BookingResponse> response = gson.fromJson(result.getResponse().getContentAsString(), listType);
        String actualStatus = result.getResponse().getContentAsString();
        System.out.println(actualStatus);
        //then
        verify(bookingRequestService, times(1)).processBookingRequestText(anyString());
        Assert.assertEquals(actualStatus,excepted);
    }
    @Test
    public void bookingRequestAsText_whenInValidTextPosted_thenReturnFailure() throws Exception {
        // Given
        String text="0800 1730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:10 2\n" +
                "2020-01-18 12:34:51 EMP002\n" +
                "2020-01-21 08:00 1";
        List<BookingResponse> bookingResponseList =new ArrayList<>();
        List<Bookings> bookingsList = new ArrayList<>();
        bookingsList.add(new Bookings("EMP001", LocalTime.of(9,00),LocalTime.of(10,00)));
        bookingResponseList.add(new BookingResponse("06-08-2023",bookingsList));
        when(this.bookingRequestService.processBookingRequestText(anyString()))
                .thenThrow(new InvalidOfficeHoursException("Invalid office hours"));

        // When
       this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/booking/text")
                        .contentType("text/plain")
                        .content(text))
                        .andExpect(status().isBadRequest());



        //then
        verify(bookingRequestService, times(1)).processBookingRequestText(anyString());
    }
    @Test
    public void bookingRequestAsText_whenInValidBookingReqPosted_thenReturnFailure() throws Exception {
        // Given
        String text="0800 1730\n" +
                "2020-01-18 10:17:06 EMP001\n" +
                "2020-01-21 09:00 2\n" +
                "2020-01-18 12:34:56 EMP002\n" +
                "2020-01-21 09:10 2\n" +
                "2020-01-18 12:34:51 EMP002\n" +
                "2020-01-21 08:00 1";
        List<BookingResponse> bookingResponseList =new ArrayList<>();
        List<Bookings> bookingsList = new ArrayList<>();
        bookingsList.add(new Bookings("EMP001", LocalTime.of(9,00),LocalTime.of(10,00)));
        bookingResponseList.add(new BookingResponse("06-08-2023",bookingsList));
        when(this.bookingRequestService.processBookingRequestText(anyString()))
                .thenThrow(new InvalidOfficeHoursException("Invalid office hours"));

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/booking/text")
                        .contentType("text/plain")
                        .content(text))
                .andExpect(status().isBadRequest());
        //then
        verify(bookingRequestService, times(1)).processBookingRequestText(anyString());
    }
}
