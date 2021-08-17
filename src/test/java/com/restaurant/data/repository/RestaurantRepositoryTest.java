package com.restaurant.data.repository;

import com.restaurant.data.entity.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void shouldRetrieveAllRestaurants() {
        RestaurantEntity restaurantEntity =
                RestaurantEntity.builder()
                        .restaurantName("Little Italy")
                        .location("Milan")
                        .build();

        testEntityManager.persist(restaurantEntity);
        testEntityManager.flush();

        List<RestaurantEntity> restaurants = restaurantRepository.findAll();

        assertEquals(3, restaurants.size());
    }

    @Test
    void shouldRetrieveRestaurantByNameAndLocation() {
        String restaurantName = "Little Italy";
        String location = "Milan";

        RestaurantEntity restaurantEntity =
                RestaurantEntity.builder()
                        .restaurantName(restaurantName)
                        .location(location)
                        .build();
        testEntityManager.persist(restaurantEntity);
        testEntityManager.flush();

        Optional<RestaurantEntity> restaurantRetrieved =
                restaurantRepository.findRestaurantEntityByRestaurantNameAndLocation(restaurantName, location);

        assertTrue(restaurantRetrieved.isPresent());
        assertEquals(restaurantName, restaurantRetrieved.get().getRestaurantName());
        assertEquals(location, restaurantRetrieved.get().getLocation());
    }

    @Test
    void shouldRetrieveRestaurantById() {
        Optional<RestaurantEntity> restaurantEntity = restaurantRepository.findById(1L);
        assertTrue(restaurantEntity.isPresent());
    }

}