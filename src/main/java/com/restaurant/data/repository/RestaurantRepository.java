package com.restaurant.data.repository;

import com.restaurant.data.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findRestaurantEntityByRestaurantNameAndLocation(String name, String location);

}
