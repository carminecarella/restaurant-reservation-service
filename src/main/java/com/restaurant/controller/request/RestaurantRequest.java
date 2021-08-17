package com.restaurant.controller.request;

import lombok.Data;

import java.util.List;

/**
 * Represent a Restaurant request
 */
@Data
public class RestaurantRequest {

    private String restaurantName;
    private String location;
    private List<Long> tableCapacities;

}
