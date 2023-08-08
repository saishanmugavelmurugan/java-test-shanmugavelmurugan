package com.lloyds.test.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class BookingRequestExceptionHandler {
    @ExceptionHandler({InvalidBookingRequestException.class,InvalidOfficeHoursException.class})
    public ProblemDetail invalidTextExceptionHandler(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setProperty("errorCategory", "ParsingText");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler({Exception.class})
    public ProblemDetail internalServerExceptionHandler(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setProperty("errorCategory", "InternalServerError");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
