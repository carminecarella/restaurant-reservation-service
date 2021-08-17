package com.restaurant.service;

import com.restaurant.domain.Availability;

import java.time.LocalDate;

public interface AvailabilityService {

    Availability getAvailabilityByCapacityAndDate(Long restaurantId, Long capacity, LocalDate date);

    Availability getAvailabilityByCapacityAndDateRange(Long restaurantId, Long capacity, LocalDate start, LocalDate end);

}
