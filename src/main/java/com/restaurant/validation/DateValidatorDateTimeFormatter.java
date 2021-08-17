package com.restaurant.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Validate a date to be in the valid format dd-MM-yyyy
 * and validate a date range in the valid format and a valid range
 * (start date before end date)
 */
@Component
public class DateValidatorDateTimeFormatter implements DateValidator {

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String PARAM_NAME = "date";
    public static final String PARAM_NAME_DATE_FROM = "dateFrom";
    public static final String PARAM_NAME_DATE_UNTIL = "dateUntil";
    public static final String FORMAT_MESSAGE = "The query param %s should be follow the format %s";
    public static final String WRONG_RANGE_MESSAGE = "%s should be before %s";

    @Override
    public String getDateFormat() {
        return DATE_FORMAT;
    }

    /**
     * Validate a string to be a valid date
     * @param date
     * @throws RequestValidationException
     */
    @Override
    public void validate(String date) throws RequestValidationException {
        Map<String, String> errors = new HashMap<>();

        if(!isValidDate(date)) {
            errors.put(PARAM_NAME, String.format(FORMAT_MESSAGE, PARAM_NAME, DATE_FORMAT));
        }

        if (errors.size() > 0) {
            throw new RequestValidationException(errors);
        }
    }

    /**
     * Validate start and end dates to be in the valid format and in a valid range (start before end)
     * @param start date
     * @param end date
     * @throws RequestValidationException
     */
    @Override
    public void validateDateRange(String start, String end) throws RequestValidationException {
        Map<String, String> errors = new HashMap<>();

        if(!isValidDate(start)) {
            errors.put(PARAM_NAME_DATE_FROM, String.format(FORMAT_MESSAGE, PARAM_NAME_DATE_FROM, DATE_FORMAT));
        }

        if(!isValidDate(end)) {
            errors.put(PARAM_NAME_DATE_UNTIL, String.format(FORMAT_MESSAGE, PARAM_NAME_DATE_UNTIL, DATE_FORMAT));
        }

        if(isValidDate(start) && isValidDate(end)) {
            LocalDate dateFrom = LocalDate.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT));
            LocalDate dateUntil = LocalDate.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT));

            if(!dateFrom.isBefore(dateUntil)) {
                errors.put(PARAM_NAME_DATE_FROM, String.format(WRONG_RANGE_MESSAGE, PARAM_NAME_DATE_FROM, PARAM_NAME_DATE_UNTIL));
            }
        }

        if (errors.size() > 0) {
            throw new RequestValidationException(errors);
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}