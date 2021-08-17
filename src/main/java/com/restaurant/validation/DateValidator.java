package com.restaurant.validation;

public interface DateValidator {

    String getDateFormat();

    void validate(String date) throws RequestValidationException;

    void validateDateRange(String start, String end) throws RequestValidationException;

}
