package com.lloyds.test.booking.api;

import com.lloyds.test.booking.model.BookingResponse;
import com.lloyds.test.booking.service.BookingRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/booking")
public class BookingRequestController {

    private BookingRequestService bookingRequestService;

    public BookingRequestController(final BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;
    }

    /**
     *
     * @param requestText
     * @return List<BookingResponse>
     */
    @Operation(summary = "Post meeting request as text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of meeting booking request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Invalid text posted",
                    content = @Content)})
    @PostMapping("/text")
    public List<BookingResponse> bookingRequestAsText(@RequestBody(required = true) final String requestText) {
        return bookingRequestService.processBookingRequestText(requestText);
    }
}
