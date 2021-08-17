package com.restaurant.service;

import com.restaurant.controller.request.RestaurantRequest;
import com.restaurant.domain.Restaurant;
import com.restaurant.validation.RestaurantSameNameAndLocationException;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();

    Optional<Restaurant> getRestaurantById(Long restaurantId);

    Optional<Restaurant> createRestaurant(RestaurantRequest restaurantRequest) throws RestaurantSameNameAndLocationException;

}