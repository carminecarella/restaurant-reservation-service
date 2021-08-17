package com.restaurant.controller.request;

import lombok.Data;

/**
 * Represent a Reservation request
 */
@Data
public class ReservationRequest {

    private Long restaurantId;
    private Long customerId;
    private Long partySize;
    private Long tableId;

}
