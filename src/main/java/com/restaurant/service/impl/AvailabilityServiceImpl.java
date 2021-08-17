package com.restaurant.service.impl;

import com.restaurant.data.entity.ReservationEntity;
import com.restaurant.data.entity.RestaurantEntity;
import com.restaurant.data.repository.ReservationRepository;
import com.restaurant.data.repository.RestaurantRepository;
import com.restaurant.domain.Availability;
import com.restaurant.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public AvailabilityServiceImpl(RestaurantRepository restaurantRepository,
                                   ReservationRepository reservationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Return tables availabilities by exact capacity and date
     * @param restaurantId
     * @param capacity
     * @param date
     * @return Availability
     */
    public Availability getAvailabilityByCapacityAndDate(Long restaurantId, Long capacity, LocalDate date) {

        Map<LocalDate, List<Long>> availabilityDates = new HashMap<>();

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(restaurantId);
        String restaurantName = restaurant.get().getRestaurantName();

        List<ReservationEntity> restaurantReservations =
                reservationRepository.findReservationEntityByRestaurantEntity_RestaurantId(restaurantId);

        List<Long> availableTables = getAvailableTableForGivenDate(restaurantReservations, restaurant, capacity, date);
        availabilityDates.put(date, availableTables);

        return Availability.builder()
                .restaurantId(restaurantId)
                .restaurantName(restaurantName)
                .capacity(capacity)
                .availabilityDates(availabilityDates)
                .build();
    }

    /**
     * Return tables availabilities by exact capacity and date range
     * @param restaurantId
     * @param capacity
     * @param start
     * @param end
     * @return Availability
     */
    @Override
    public Availability getAvailabilityByCapacityAndDateRange(Long restaurantId, Long capacity,
                                                              LocalDate start, LocalDate end) {

        List<LocalDate> dates = start.datesUntil(end.plusDays(1)).collect(Collectors.toList());
        Map<LocalDate, List<Long>> availabilityDates = new HashMap<>();

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(restaurantId);
        String restaurantName = restaurant.get().getRestaurantName();

        List<ReservationEntity> restaurantReservations =
                reservationRepository.findReservationEntityByRestaurantEntity_RestaurantId(restaurantId);

        for(LocalDate date: dates) {
            List<Long> availableTables = getAvailableTableForGivenDate(restaurantReservations, restaurant, capacity, date);
            availabilityDates.put(date, availableTables);
        }

        return Availability.builder()
                .restaurantId(restaurantId)
                .restaurantName(restaurantName)
                .capacity(capacity)
                .availabilityDates(availabilityDates)
                .build();
    }

    private List<Long> getAvailableTableForGivenDate( List<ReservationEntity> restaurantReservations,
                                                      Optional<RestaurantEntity> restaurant,
                                                      Long capacity,
                                                      LocalDate date) {
        List<Long> tablesReservedForGivenDate =
                restaurantReservations.stream()
                        .filter(r -> r.getReservationDate().toLocalDate().equals(date))
                        .map(r -> r.getRestaurantTableEntity().getTableId()).collect(Collectors.toList());

        //tables not reserved for the date requested that match the capacity
        List<Long> availableTables = restaurant.get().getTables().stream()
                .filter(t -> (!tablesReservedForGivenDate.contains(t.getTableId())))
                .filter(t -> t.getCapacity() == capacity)
                .map(t -> t.getTableId())
                .collect(Collectors.toList());

        return availableTables;
    }

}
