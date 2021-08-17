package com.restaurant.controller;

import com.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.restaurant.domain.Restaurant.builder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    public void shouldReturnAllRestaurants() throws Exception {
        //given:
        String expectedResponse = "[{name: Tacos, location: London}, {name: CrazyPizza, location: Paris}]";

        //when:
        when(restaurantService.getAllRestaurants()).thenReturn(
                List.of(builder().name("Tacos").location("London").build(),
                        builder().name("CrazyPizza").location("Paris").build())
        );

        //then:
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void shouldReturnRestaurantById() throws Exception {
        //given:
        String expectedResponse = "{name: Tacos, location: London}";

        //when:
        when(restaurantService.getRestaurantById(1L))
                .thenReturn(Optional.of(builder().name("Tacos").location("London").build()));

        //then:
        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void shouldReturnNotFoundWhenRestaurantByIdDoesNotExists() throws Exception {
        //when:
        when(restaurantService.getRestaurantById(1L)).thenReturn(Optional.empty());

        //then:
        mockMvc.perform(get("/api/restaurants/1")).andExpect(status().isNotFound());
    }

}