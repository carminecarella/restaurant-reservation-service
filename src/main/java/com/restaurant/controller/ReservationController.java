package com.restaurant.controller;

import com.restaurant.controller.request.ReservationRequest;
import com.restaurant.domain.RestaurantReservation;
import com.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(params = "restaurant")
    public ResponseEntity<List<RestaurantReservation>> getAllReservation(@RequestParam("restaurant") Long restaurantId) {

        List<RestaurantReservation> restaurantReservations = reservationService.getAllReservation(restaurantId);

        if(restaurantReservations.isEmpty()) {
            new ResponseEntity<>(List.of(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(restaurantReservations, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> cancelReservation(@PathVariable Long id) {
        Long cancelledReservation = reservationService.cancelReservation(id);
        return new ResponseEntity<>(cancelledReservation, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> amendReservation(@PathVariable Long id,
                                                 @RequestBody ReservationRequest reservationRequest) {
        Long amendedReservation = reservationService.amendReservation(id, reservationRequest);
        return new ResponseEntity<>(amendedReservation, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestaurantReservation> makeReservation(@RequestBody ReservationRequest reservationRequest) {
        RestaurantReservation reservation = reservationService.makeReservation(reservationRequest);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

}
