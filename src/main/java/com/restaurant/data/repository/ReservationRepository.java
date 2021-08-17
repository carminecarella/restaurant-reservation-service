package com.restaurant.data.repository;

import com.restaurant.data.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findReservationEntityByRestaurantEntity_RestaurantId(Long restaurantId);

}
