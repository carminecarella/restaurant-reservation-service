package com.restaurant.controller;

import com.restaurant.domain.Availability;
import com.restaurant.service.AvailabilityService;
import com.restaurant.validation.DateValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @MockBean
    private DateValidator dateValidator;

    @Test
    public void shouldReturnAvailabilities() throws Exception {
        //given:
        String expectedResponse = "{restaurantId:1,restaurantName:Tacos,capacity:2,availabilityDates:{2020-12-24:[1]}}";

        //when:
        when(dateValidator.getDateFormat()).thenReturn("dd-MM-yyyy");

        when(availabilityService.getAvailabilityByCapacityAndDate(1L, 2L, LocalDate.of(2020, 12, 24))).thenReturn(
                Availability.builder()
                        .restaurantId(1L)
                        .restaurantName("Tacos")
                        .availabilityDates(Map.of(LocalDate.of(2020, 12, 24), List.of(1L)))
                        .capacity(2L)
                        .build()
        );

        //then:
        mockMvc.perform(get("/api/availabilities")
                .param("restaurant", "1")
                .param("capacity", "2")
                .param("date", "24-12-2020"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

}