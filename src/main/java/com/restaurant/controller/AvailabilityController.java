package com.restaurant.controller;

import com.restaurant.domain.Availability;
import com.restaurant.service.AvailabilityService;
import com.restaurant.validation.DateValidator;
import com.restaurant.validation.RequestValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final DateValidator dateValidator;

    @Autowired
    public AvailabilityController(AvailabilityService availabilityService, DateValidator dateValidator) {
        this.availabilityService = availabilityService;
        this.dateValidator = dateValidator;
    }

    @GetMapping(params = "date")
    public ResponseEntity<Availability> getAvailabilityByDate(@RequestParam("restaurant") Long restaurantId,
                                                              @RequestParam Long capacity,
                                                              @RequestParam String date) throws RequestValidationException {
        dateValidator.validate(date);

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateValidator.getDateFormat()));

        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDate(restaurantId, capacity, localDate);

        if(availabilities.getAvailabilityDates().isEmpty()) {
            new ResponseEntity<>(List.of(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Availability> getAvailabilityByDateRange(@RequestParam("restaurant") Long restaurantId,
                                                                   @RequestParam Long capacity,
                                                                   @RequestParam String dateFrom,
                                                                   @RequestParam String dateUntil) throws RequestValidationException {
        dateValidator.validateDateRange(dateFrom, dateUntil);

        LocalDate localDateFrom = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern(dateValidator.getDateFormat()));
        LocalDate localDateUntil = LocalDate.parse(dateUntil, DateTimeFormatter.ofPattern(dateValidator.getDateFormat()));

        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDateRange(restaurantId, capacity, localDateFrom, localDateUntil);

        if(availabilities.getAvailabilityDates().isEmpty()) {
            new ResponseEntity<>(List.of(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

}
