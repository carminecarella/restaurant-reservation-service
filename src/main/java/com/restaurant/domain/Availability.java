package com.restaurant.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class Availability {

    private Long restaurantId;
    private String restaurantName;
    private Long capacity;
    private Map<LocalDate, List<Long>> availabilityDates;

}
