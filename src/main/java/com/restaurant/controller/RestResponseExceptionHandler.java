package com.restaurant.controller;

import com.restaurant.validation.RequestValidationException;
import com.restaurant.validation.ErrorResponse;
import com.restaurant.validation.RestaurantSameNameAndLocationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for controllers
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle a validation exception and wrap the message in a response entity
     * @param requestValidationException
     * @param request
     * @return ResponseEntity<List<ErrorResponse>>
     */
    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<List<ErrorResponse>> handleRequestValidationException(RequestValidationException requestValidationException,
                                                                                WebRequest request) {

        List<ErrorResponse> errorResponses = new ArrayList<>();

        requestValidationException.getErrors().entrySet()
                .forEach(e -> errorResponses.add(ErrorResponse.builder().paramName(e.getKey()).message(e.getValue()).build()));

        return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle a restaurant with same name and location exception and wrap the message in a response entity
     * @param restaurantSameNameAndLocationException
     * @param request
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(RestaurantSameNameAndLocationException.class)
    public ResponseEntity<ErrorResponse> handleRequestValidationException(RestaurantSameNameAndLocationException restaurantSameNameAndLocationException,
                                                                                WebRequest request) {

        ErrorResponse errorResponses = ErrorResponse.builder().message(restaurantSameNameAndLocationException.getMessage()).build();

        return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    }

}
