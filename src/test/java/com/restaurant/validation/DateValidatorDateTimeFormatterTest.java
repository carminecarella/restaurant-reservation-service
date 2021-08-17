package com.restaurant.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DateValidatorDateTimeFormatterTest {

    @Test
    void shouldValidateDateSuccessfully() {
        //when:
        String date = "25-12-2020";
        DateValidatorDateTimeFormatter dateValidatorDateTimeFormatter = new DateValidatorDateTimeFormatter();

        //then:
        assertDoesNotThrow(() -> dateValidatorDateTimeFormatter.validate(date));
    }

    @Test
    void shouldReturnRequestValidationException() {
        //given:
        String date = "25/12/2020";
        DateValidatorDateTimeFormatter dateValidatorDateTimeFormatter = new DateValidatorDateTimeFormatter();

        //when:
        RequestValidationException ex = assertThrows(RequestValidationException.class,
                () -> dateValidatorDateTimeFormatter.validate(date));

        //then
        assertTrue(ex.getErrors().containsKey("date"));
        assertEquals("The query param date should be follow the format dd-MM-yyyy", ex.getErrors().get("date"));
    }

    @Test
    void shouldValidateDateRangeSuccessfully() {
        //when:
        String start = "25-12-2020";
        String end = "27-12-2020";
        DateValidatorDateTimeFormatter dateValidatorDateTimeFormatter = new DateValidatorDateTimeFormatter();

        //then:
        assertDoesNotThrow(() -> dateValidatorDateTimeFormatter.validateDateRange(start, end));
    }

    @Test
    void shouldReturnRequestValidationExceptionForDateRange() {
        //given:
        String start = "25/12/2020";
        String end = "27/12/2020";
        DateValidatorDateTimeFormatter dateValidatorDateTimeFormatter = new DateValidatorDateTimeFormatter();

        //when:
        RequestValidationException ex = assertThrows(RequestValidationException.class,
                () -> dateValidatorDateTimeFormatter.validateDateRange(start, end));

        //then
        assertEquals(2, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("dateFrom"));
        assertEquals("The query param dateFrom should be follow the format dd-MM-yyyy", ex.getErrors().get("dateFrom"));

        assertTrue(ex.getErrors().containsKey("dateUntil"));
        assertEquals("The query param dateUntil should be follow the format dd-MM-yyyy", ex.getErrors().get("dateUntil"));
    }

    @Test
    void shouldReturnRequestValidationExceptionWhenDateFromIsNotBeforeDateUntil() {
        //given:
        String start = "27-12-2020";
        String end = "24-12-2020";
        DateValidatorDateTimeFormatter dateValidatorDateTimeFormatter = new DateValidatorDateTimeFormatter();

        //when:
        RequestValidationException ex = assertThrows(RequestValidationException.class,
                () -> dateValidatorDateTimeFormatter.validateDateRange(start, end));

        //then
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("dateFrom"));
        assertEquals("dateFrom should be before dateUntil", ex.getErrors().get("dateFrom"));
    }

    @Test
    void shouldReturnRequestValidationExceptionWhenDateRangeIsNotValid() {
        //given:
        String start = "27-12-2020";
        String end = "24-12-2020";
        DateValidatorDateTimeFormatter dateValidatorDateTimeFormatter = new DateValidatorDateTimeFormatter();

        //when:
        RequestValidationException ex = assertThrows(RequestValidationException.class,
                () -> dateValidatorDateTimeFormatter.validateDateRange(start, end));

        //then
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().containsKey("dateFrom"));
        assertEquals("dateFrom should be before dateUntil", ex.getErrors().get("dateFrom"));
    }

}