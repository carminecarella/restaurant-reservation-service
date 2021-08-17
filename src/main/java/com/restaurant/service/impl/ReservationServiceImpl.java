package com.restaurant.service.impl;

import com.restaurant.controller.request.ReservationRequest;
import com.restaurant.data.entity.CustomerEntity;
import com.restaurant.data.entity.ReservationEntity;
import com.restaurant.data.entity.RestaurantEntity;
import com.restaurant.data.entity.RestaurantTableEntity;
import com.restaurant.data.repository.CustomerRepository;
import com.restaurant.data.repository.RestaurantTableRepository;
import com.restaurant.domain.RestaurantReservation;
import com.restaurant.service.ReservationService;
import com.restaurant.data.repository.ReservationRepository;
import com.restaurant.data.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RestaurantRepository restaurantRepository,
                                  CustomerRepository customerRepository,
                                  RestaurantTableRepository restaurantTableRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    /**
     * Cancel a reservation by id
     * @param reservationId
     * @return the id of the reservation deleted
     */
    @Override
    public Long cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
        return reservationId;
    }

    /**
     * Amend a reservation by id
     * @param reservationId
     * @param reservationRequest
     * @return the id of the reservation amended
     */
    @Override
    public Long amendReservation(Long reservationId, ReservationRequest reservationRequest) {
        Optional<ReservationEntity> optionalReservationEntity = reservationRepository.findById(reservationId);

        if(optionalReservationEntity.isPresent()) {
            ReservationEntity reservationEntity = optionalReservationEntity.get();
            reservationEntity.setPartySize(reservationRequest.getPartySize());

            Optional<RestaurantTableEntity> restaurantTableEntity =
                    restaurantTableRepository.findById(reservationRequest.getTableId());

            reservationEntity.setRestaurantTableEntity(restaurantTableEntity.get());

            reservationEntity.setReservationDate(Date.valueOf(LocalDate.now()));
            reservationRepository.save(reservationEntity);
        }

        return reservationId;
    }

    /**
     * Create a new reservation
     * @param reservationRequest
     * @return RestaurantReservation created
     */
    @Override
    public RestaurantReservation makeReservation(ReservationRequest reservationRequest) {

        Optional<RestaurantEntity> optionalRestaurantEntity =
                restaurantRepository.findById(reservationRequest.getRestaurantId());

        Optional<CustomerEntity> optionalCustomerEntity =
                customerRepository.findById(reservationRequest.getCustomerId());

        Optional<RestaurantTableEntity> optionalRestaurantTableEntity =
                restaurantTableRepository.findById(reservationRequest.getTableId());

        ReservationEntity reservationEntity =
                ReservationEntity.builder()
                        .reservationDate(Date.valueOf(LocalDate.now()))
                        .partySize(reservationRequest.getPartySize())
                        .restaurantEntity(optionalRestaurantEntity.get())
                        .customerEntity(optionalCustomerEntity.get())
                        .restaurantTableEntity(optionalRestaurantTableEntity.get())
                        .build();

        ReservationEntity savedReservationEntity = reservationRepository.save(reservationEntity);

        return convertToRestaurantReservation(savedReservationEntity);
    }

    /**
     * Return all reservations for a given restaurant
     * @param restaurantId
     * @return List<RestaurantReservation>
     */
    @Override
    public List<RestaurantReservation> getAllReservation(Long restaurantId) {
        List<ReservationEntity> reservationEntities =
                reservationRepository.findReservationEntityByRestaurantEntity_RestaurantId(restaurantId);

        return reservationEntities.stream()
                .map(re -> convertToRestaurantReservation(re))
                .collect(Collectors.toList());

    }

    private RestaurantReservation convertToRestaurantReservation(ReservationEntity reservationEntity) {
        return RestaurantReservation.builder()
                .reservationId(reservationEntity.getReservationId())
                .restaurantName(reservationEntity.getRestaurantEntity().getRestaurantName())
                .customerId(reservationEntity.getCustomerEntity().getCustomerId())
                .customerName(reservationEntity.getCustomerEntity().getCustomerName())
                .reservationDate(reservationEntity.getReservationDate().toLocalDate())
                .partySize(reservationEntity.getPartySize())
                .tableId(reservationEntity.getRestaurantTableEntity().getTableId())
                .build();
    }

}
