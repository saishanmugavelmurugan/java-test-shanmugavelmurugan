package com.lloyds.test.booking.service;

import com.lloyds.test.booking.model.BookingResponse;

import java.util.List;

public interface BookingRequestService {
    /**
     * This method will process the booking request as input text  and return the Json resonse.
     * validate the booking request text with valid office hours
     * Remove the overlapping of meeting request
     * @param requestText
     * @return List<BookingResponse>
     */
    List<BookingResponse> processBookingRequestText(String requestText);
}
