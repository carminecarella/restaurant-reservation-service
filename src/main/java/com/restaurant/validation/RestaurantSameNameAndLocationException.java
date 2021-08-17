package com.restaurant.validation;

import lombok.Data;

/**
 * Represent a same name and location exception handled by global handler exception
 */
@Data
public class RestaurantSameNameAndLocationException extends Exception {

    private final String message;

}
