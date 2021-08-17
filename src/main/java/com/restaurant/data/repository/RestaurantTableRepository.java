package com.restaurant.data.repository;

import com.restaurant.data.entity.RestaurantTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTableEntity, Long>  {
}
