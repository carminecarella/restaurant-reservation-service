package com.restaurant.validation;

import lombok.Builder;
import lombok.Data;

/**
 * Represent a response message to be returned by the controller in case
 * of responses that is not 200
 */
@Builder
@Data
public class ErrorResponse {

    private String paramName;
    private String message;

}
