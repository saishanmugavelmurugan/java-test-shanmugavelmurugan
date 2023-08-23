package com.lloyds.test.booking.service;

import com.lloyds.test.booking.exception.InvalidBookingRequestException;
import com.lloyds.test.booking.exception.InvalidOfficeHoursException;
import com.lloyds.test.booking.model.BookingRequest;
import com.lloyds.test.booking.model.BookingResponse;
import com.lloyds.test.booking.model.Bookings;
import com.lloyds.test.booking.model.OfficeHours;
import com.lloyds.test.booking.utils.BookingRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.lloyds.test.booking.constants.BookingConstants.*;
import static java.util.stream.Collectors.*;

@Service
public class BookingRequestServiceImpl implements BookingRequestService {

    private static final Logger logger = LoggerFactory.getLogger(BookingRequestServiceImpl.class);

    public BookingRequestUtils bookingRequestUtils;

    public BookingRequestServiceImpl(BookingRequestUtils bookingRequestUtils) {
        this.bookingRequestUtils = bookingRequestUtils;
    }



    /**
     * This method will process the booking request as input text  and return the Json resonse.
     * validate the booking request text with valid office hours
     * Remove the overlapping of meeting request
     *
     * @param requestText
     * @return List<BookingResponse>
     */
    @Override
    public List<BookingResponse> processBookingRequestText(final String requestText) {
        //prase Given  string with linefeed or return
        List<String> lineList = Stream.of(requestText.split(LINE_FEED_REG_PATTERN)).map(String::trim).collect(toList());
        //get office hours
        OfficeHours officeHours = parseOfficeHours(lineList.get(0), START_END_HOURS_REG_PATTERN);
        //extract booking request
        List<BookingRequest> bookingRequestList = parseBookingRequest(lineList);
        Map<LocalDate, List<BookingRequest>> booking = bookingRequestList.stream().collect(groupingBy(BookingRequest::getMeetingStartDate, collectingAndThen(toList(), list -> {
            return list.stream()
                    .sorted(Comparator.comparing(BookingRequest::getSubmissionTime)) //.thenComparing(BookingRequest::getSubmissionTime))
                    .filter(req -> bookingRequestUtils.isValidTimeInverval(officeHours.getOfficeStartHours(), officeHours.getOfficeEndHours(), req.getMeetingStartTime()) &&
                            req.getMeetingEndTime().compareTo(officeHours.getOfficeEndHours()) <= 0)
                    .collect(Collectors.toList());
        })));

        Map<LocalDate, List<Bookings>> finalList = removeOverlapping(booking);
        return finalList.entrySet().stream().map(entry -> new BookingResponse(entry.getKey().toString(), entry.getValue())).toList();
    }


    private Map<LocalDate, List<Bookings>> removeOverlappingv1(Map<LocalDate, List<BookingRequest>> booking){
        Map<LocalDate, List<Bookings>> filnalMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<BookingRequest>> entry : booking.entrySet()) {
            Set<BookingRequest> overlappingList= IntStream.range(0, entry.getValue().size())
                    .filter(i -> findOverlap(entry.getValue().get(i),
                            entry.getValue().subList(0, entry.getValue().size()))).mapToObj(i->entry.getValue().get(i)).collect(Collectors.toSet());

            List<Bookings> bookingsList = entry.getValue().stream().filter(req -> !overlappingList.contains(req))
                    .map(req -> new Bookings(req.getEmployeeId(), req.getMeetingStartTime(), req.getMeetingEndTime()))
                    .collect(Collectors.toList());
            filnalMap.put(entry.getKey(), bookingsList);
        }
        return filnalMap;
    }
    public boolean findOverlap(final BookingRequest element, final List<BookingRequest> list) {

        return list.stream().anyMatch(l-> l.getSubmissionTime().compareTo(element.getSubmissionTime())!=0 &&
                (element.getMeetingStartTime().compareTo(l.getMeetingStartTime())>=0 && element.getMeetingStartTime().compareTo(l.getMeetingEndTime())<0? ((element.getSubmissionTime().compareTo(l.getSubmissionTime()) <= 0)? false:true):false));

    }
    /**
     * Remove the overlapping meeting time intervals from the given list grouped by meeting date.
     *
     * @param booking  -Map<LocalDate, List<BookingRequest>>
     * @return Map<LocalDate, List<Bookings>>
     */
    private Map<LocalDate, List<Bookings>> removeOverlapping(Map<LocalDate, List<BookingRequest>> booking) {
        Map<LocalDate, List<Bookings>> filnalMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<BookingRequest>> entry : booking.entrySet()) {
            Set<BookingRequest> overlappingList = new HashSet<>();
            for (int i = 0; i < entry.getValue().size(); i++) {
                for (int j = 0; j < entry.getValue().size(); j++) {

                    if (entry.getValue().get(i).getSubmissionTime().compareTo(entry.getValue().get(j).getSubmissionTime()) != 0 &&
                            bookingRequestUtils.isValidTimeInverval(entry.getValue().get(j).getMeetingStartTime(), entry.getValue().get(j).getMeetingEndTime(), entry.getValue().get(i).getMeetingStartTime())) {
                        if (entry.getValue().get(i).getSubmissionTime().compareTo(entry.getValue().get(j).getSubmissionTime()) <= 0) {
                            overlappingList.add(entry.getValue().get(j));
                        } else {
                            overlappingList.add(entry.getValue().get(i));
                        }

                    }
                }

            }

            List<Bookings> bookingsList = entry.getValue().stream().filter(req -> !overlappingList.contains(req))
                    .map(req -> new Bookings(req.getEmployeeId(), req.getMeetingStartTime(), req.getMeetingEndTime()))
                    .collect(Collectors.toList());
            filnalMap.put(entry.getKey(), bookingsList);
        }
        return filnalMap;
    }

    /**
     * Method used to parse the office hours from given text.
     * office hours should be in pattern on <HHmm HHmm> -startHours endHours
     *
     * @param officeHours -  HHmm HHmm
     * @param pattern     - RegPattern
     * @return OfficeHours
     */
    private OfficeHours parseOfficeHours(String officeHours, String pattern) {
        Matcher matcher = this.bookingRequestUtils.getMatcher(officeHours, pattern);
        if (matcher.find()) {
            return new OfficeHours(LocalTime.parse(matcher.group(1).trim(), DateTimeFormatter.ofPattern(HOURS_MIN)),
                    LocalTime.parse(matcher.group(2).trim(), DateTimeFormatter.ofPattern(HOURS_MIN)));
        } else {
            logger.error("Invalid text given for praising office hours: " + officeHours);
            throw new InvalidOfficeHoursException("Invalid office hours pattern (or) unable parse the office hours from the text:" + officeHours);
        }
    }

    /**
     * User submitted meeting requests are parsed and format of each request should be Submitted time EmployeeId and meeting request time and hour
     * Pattern to follow as yyyy-MM-dd HH:mm:ss EMPxxx  and yyyy-MM-dd HH:mm x(hours)
     *
     * @param lineList - Parsed text as string list
     * @return List<BookingRequest>
     */
    private List<BookingRequest> parseBookingRequest(final List<String> lineList) {
        List<BookingRequest> bookingRequestList = new ArrayList<>();
        for (int i = 1; i < lineList.size(); i += 2) {
            if (lineList.get(i).contains(EMP)) {
                Matcher empMatcher = bookingRequestUtils.getMatcher(lineList.get(i), SUBMITTED_TIME_REG_PATTERN);
                Matcher hsMatcher = bookingRequestUtils.getMatcher(lineList.get(i + 1), BOOKING_REQ_REG_PATTERN);
                if (empMatcher.find() && hsMatcher.find()) {
                    LocalDateTime requestStartTime = LocalDateTime.parse(hsMatcher.group(1).trim(), DateTimeFormatter.ofPattern(MEETING_REQ_DATE_FORMAT));
                    LocalDateTime requestEndTime = LocalDateTime.parse(hsMatcher.group(1).trim(), DateTimeFormatter.ofPattern(MEETING_REQ_DATE_FORMAT)).plusHours(Long.valueOf(hsMatcher.group(2)));
                    bookingRequestList.add(new BookingRequest(
                            empMatcher.group(2),
                            LocalDateTime.parse(empMatcher.group(1).trim(), DateTimeFormatter.ofPattern(SUBMITTED_DATE_FORMAT)),
                            requestStartTime.toLocalDate(),
                            requestStartTime.toLocalTime(),
                            requestStartTime,
                            requestEndTime,
                            requestEndTime.toLocalTime(),
                            Long.valueOf(hsMatcher.group(2))
                    ));
                } else {
                    logger.error("Invalid format of booking request received:" + lineList.get(i) + "::" + lineList.get(i + 1));
                    throw new InvalidBookingRequestException("Invalid format of booking request received:" + lineList.get(i) + "::" + lineList.get(i + 1));
                }
            } else {
                logger.error("Invalid request received for processing: " + lineList.get(i) + "::" + lineList.get(i + 1));
                throw new InvalidBookingRequestException("Invalid request received for processing: " + lineList.get(i) + "::" + lineList.get(i + 1));
            }
        }

        return bookingRequestList;
    }
}