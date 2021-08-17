package com.restaurant.service.impl;

import com.restaurant.data.entity.RestaurantEntity;
import com.restaurant.data.repository.RestaurantRepository;
import com.restaurant.data.repository.RestaurantTableRepository;
import com.restaurant.domain.Restaurant;
import com.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class RestaurantServiceTest {

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private RestaurantTableRepository restaurantTableRepository;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        RestaurantEntity restaurantEntity1 =
                RestaurantEntity.builder()
                        .restaurantId(1L)
                        .restaurantName("restaurant1")
                        .location("Milan")
                        .build();

        RestaurantEntity restaurantEntity2 =
                RestaurantEntity.builder()
                        .restaurantId(1L)
                        .restaurantName("restaurant2")
                        .location("London")
                        .build();

        Mockito.when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity1, restaurantEntity2));
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity1));

        restaurantService = new RestaurantServiceImpl(restaurantRepository, restaurantTableRepository);
    }

    @Test
    void shouldReturnAllRestaurants() {
        //when:
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        //then:
        assertEquals(2, restaurants.size());
    }

    @Test
    void shouldReturnRestaurantById() {
        //when:
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(1L);

        //then:
        assertTrue(restaurant.isPresent());
        assertEquals("restaurant1", restaurant.get().getName());
        assertEquals("Milan", restaurant.get().getLocation());
    }

    @Test
    void shouldReturnEmptyWhenRestaurantByIdIsNotFound() {
        //when:
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(2L);

        //then:
        assertTrue(restaurant.isEmpty());
    }

}