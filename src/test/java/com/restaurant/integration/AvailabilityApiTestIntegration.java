package com.restaurant.integration;

import com.restaurant.domain.Availability;
import com.restaurant.validation.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
class AvailabilityApiTestIntegration {

    @LocalServerPort
    private int port;

    @Test
    public void shouldReturnAvailabilitiesByCapacityAndDate() {
        Availability availabilities =
                get(createURLWithPort("/api/availabilities?restaurant=1&capacity=2&date=24-12-2020"))
               .then()
               .statusCode(200)
               .extract().as(Availability.class);

        assertEquals(1, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 24)).size());
        assertEquals("Tacos", availabilities.getRestaurantName());
        assertEquals(2, availabilities.getCapacity());
    }

    @Test
    public void shouldReturnEmptyResponseWhenNoAvailabilitiesAreFoundByCapacityAndDate() {
        Availability availabilities =
                get(createURLWithPort("/api/availabilities?restaurant=1&capacity=6&date=25-12-2020"))
                        .then()
                        .statusCode(200)
                        .extract().as(Availability.class);

        assertEquals(0, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 25)).size());
    }

    @Test
    public void shouldReturn400WhenDateIsNotValidFormat() {
        ErrorResponse[] errorResponse =
                get(createURLWithPort("/api/availabilities?restaurant=1&capacity=6&date=25/12/2020"))
                        .then()
                        .statusCode(400)
                        .extract().as(ErrorResponse[].class);

        assertEquals("date", errorResponse[0].getParamName());
        assertEquals("The query param date should be follow the format dd-MM-yyyy", errorResponse[0].getMessage());
    }

    @Test
    public void shouldReturnAvailabilitiesByCapacityAndDateRange() {
        Availability availabilities =
                get(createURLWithPort("/api/availabilities?restaurant=1&capacity=3&dateFrom=24-12-2020&dateUntil=26-12-2020"))
                        .then()
                        .statusCode(200)
                        .extract().as(Availability.class);

        assertEquals("Tacos", availabilities.getRestaurantName());
        assertEquals(3, availabilities.getCapacity());
        assertEquals(2, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 24)).size());
        assertEquals(3, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 25)).size());
        assertEquals(3, availabilities.getAvailabilityDates().get(LocalDate.of(2020, 12, 26)).size());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}