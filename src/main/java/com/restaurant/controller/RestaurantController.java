package com.restaurant.controller;

import com.restaurant.controller.request.RestaurantRequest;
import com.restaurant.domain.Restaurant;
import com.restaurant.service.RestaurantService;
import com.restaurant.validation.RestaurantSameNameAndLocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/restaurants", produces = "application/json")
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        if(restaurants.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantService.getRestaurantById(id);

        if(optionalRestaurant.isPresent()) {
            return new ResponseEntity<>(optionalRestaurant.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRequest restaurantRequest)
            throws RestaurantSameNameAndLocationException {
        Optional<Restaurant> optionalRestaurant = restaurantService.createRestaurant(restaurantRequest);
        return new ResponseEntity<>(optionalRestaurant.get(), HttpStatus.CREATED);
    }

}
