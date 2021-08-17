package com.restaurant.data.repository;

import com.restaurant.data.entity.ReservationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void shouldFindAllReservationByRestaurantId() {
        List<ReservationEntity> restaurantReservations =
                reservationRepository.findReservationEntityByRestaurantEntity_RestaurantId(1L);

        assertEquals(3, restaurantReservations.size());
    }

}