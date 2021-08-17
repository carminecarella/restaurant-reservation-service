package com.restaurant.validation;

import lombok.Data;

import java.util.Map;

/**
 * Represent a validation exception handled by global handler exception
 */
@Data
public class RequestValidationException extends Exception {

    private Map<String, String> errors;

    public RequestValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

}
