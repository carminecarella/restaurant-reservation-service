package com.restaurant.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class RestaurantReservation {

    private Long reservationId;
    private String restaurantName;
    private LocalDate reservationDate;
    private Long partySize;
    private Long customerId;
    private String customerName;
    private Long tableId;

}
