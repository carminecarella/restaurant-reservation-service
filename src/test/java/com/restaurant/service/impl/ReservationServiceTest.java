package com.restaurant.service.impl;

import com.restaurant.data.entity.CustomerEntity;
import com.restaurant.data.entity.ReservationEntity;
import com.restaurant.data.entity.RestaurantEntity;
import com.restaurant.data.entity.RestaurantTableEntity;
import com.restaurant.data.repository.ReservationRepository;
import com.restaurant.data.repository.RestaurantRepository;
import com.restaurant.domain.Availability;
import com.restaurant.service.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class ReservationServiceTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private RestaurantRepository restaurantRepository;

    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        RestaurantTableEntity tableEntity1 = RestaurantTableEntity.builder().tableId(1).capacity(4).build();
        RestaurantTableEntity tableEntity2 = RestaurantTableEntity.builder().tableId(2).capacity(6).build();
        RestaurantTableEntity tableEntity3 = RestaurantTableEntity.builder().tableId(3).capacity(2).build();
        RestaurantTableEntity tableEntity4 = RestaurantTableEntity.builder().tableId(4).capacity(4).build();

        RestaurantEntity restaurantEntity =
                RestaurantEntity.builder()
                        .restaurantId(1L)
                        .restaurantName("Restaurant1")
                        .location("London")
                        .tables(Set.of(tableEntity1, tableEntity2, tableEntity3, tableEntity4))
                        .build();

        ReservationEntity reservationEntity =
                ReservationEntity.builder()
                        .reservationId(1L)
                        .partySize(2)
                        .reservationDate(Date.valueOf(LocalDate.of(2020, 12, 24)))
                        .restaurantEntity(restaurantEntity)
                        .customerEntity(CustomerEntity.builder().customerId(1).customerName("Jack Green").build())
                        .restaurantTableEntity(tableEntity3)
                        .build();

        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));

        Mockito.when(reservationRepository.findReservationEntityByRestaurantEntity_RestaurantId(1L))
                .thenReturn(List.of(reservationEntity));

        availabilityService = new AvailabilityServiceImpl(restaurantRepository, reservationRepository);
    }

    @Test
    void shouldReturnAvailabilityForARestaurantByCapacityAndDate() {
        //given:
        Long capacity = 6L;
        LocalDate requestedDate = LocalDate.of(2020, 12, 24);

        //when:
        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDate(1L, capacity, requestedDate);

        //then:
        assertEquals(1, availabilities.getAvailabilityDates().get(requestedDate).size());
    }

    @Test
    void shouldReturnNoAvailabilitiesWhenATableIsAlreadyReserved() {
        //given:
        Long capacity = 2L;
        LocalDate requestedDate = LocalDate.of(2020, 12, 24);

        //when:
        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDate(1L, capacity, requestedDate);

        //then:
        assertTrue(availabilities.getAvailabilityDates().get(requestedDate).isEmpty());
    }

    @Test
    void shouldReturnAvailabilitiesForATableReservedWhenDifferentDateIsRequested() {
        //given:
        Long capacity = 2L;
        LocalDate requestedDate = LocalDate.of(2020, 12, 25);

        //when:
        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDate(1L, capacity, requestedDate);

        //then:
        assertEquals(1, availabilities.getAvailabilityDates().get(requestedDate).size());
    }

    @Test
    void shouldReturnMultipleAvailabilities() {
        //given:
        Long capacity = 4L;
        LocalDate requestedDate = LocalDate.of(2020, 12, 24);

        //when:
        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDate(1L, capacity, requestedDate);

        //then:
        assertEquals(2, availabilities.getAvailabilityDates().get(requestedDate).size());
    }

    @Test
    void shouldReturnNoAvailabilitiesWhenThereAreNotTablesForTheGivenCapacity() {
        //given:
        Long capacity = 10L;
        LocalDate requestedDate = LocalDate.of(2020, 12, 24);

        //when:
        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDate(1L, capacity, requestedDate);

        //then:
        assertTrue(availabilities.getAvailabilityDates().get(requestedDate).isEmpty());
    }

    @Test
    void shouldReturnAvailabilityForARestaurantByRangeDate() {
        //given:
        Long capacity = 4L;
        LocalDate requestedDateFrom = LocalDate.of(2020, 12, 24);
        LocalDate requestedDateUntil = LocalDate.of(2020, 12, 26);

        //when:
        Availability availabilities =
                availabilityService.getAvailabilityByCapacityAndDateRange(1L, capacity, requestedDateFrom, requestedDateUntil);

        //then:
        assertEquals(2, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 24)).size());
        assertEquals(2, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 25)).size());
        assertEquals(2, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 26)).size());
    }
}