package com.restaurant.service;

import com.restaurant.controller.request.ReservationRequest;
import com.restaurant.domain.RestaurantReservation;

import java.util.List;

public interface ReservationService {

    List<RestaurantReservation> getAllReservation(Long restaurantId);

    Long cancelReservation(Long reservationId);

    Long amendReservation(Long reservationId, ReservationRequest reservationRequest);

    RestaurantReservation makeReservation(ReservationRequest reservationRequest);

}
