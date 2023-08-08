package com.lloyds.test.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidOfficeHoursException extends RuntimeException{
    public InvalidOfficeHoursException(){ }
    public InvalidOfficeHoursException(final String s) {
        super(s);
    }

    public InvalidOfficeHoursException(final Throwable cause) {
        super(cause);
    }
}
