package com.restaurant.integration;

import com.restaurant.domain.RestaurantReservation;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
class ReservationApiTestIntegration {

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setup() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    public void shouldMakeReservation() {
        String requestBody = "{\n" +
                "  \"restaurantId\": \"2\",\n" +
                "  \"customerId\": \"1\",\n" +
                "  \"tableId\": \"3\",\n" +
                "  \"partySize\": \"4\"\n}";

        RestaurantReservation createdReservation = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(createURLWithPort("/api/reservations"))
                .then()
                .extract().as(RestaurantReservation.class);

        RestaurantReservation[] reservations =
                get(createURLWithPort("/api/reservations?restaurant=2"))
                        .then()
                        .statusCode(200)
                        .extract().as(RestaurantReservation[].class);

        RestaurantReservation reservation = getReservationById(reservations, createdReservation.getReservationId());

        assertEquals(createdReservation.getReservationId(), reservation.getReservationId());
        assertEquals(createdReservation.getRestaurantName(), reservation.getRestaurantName());
        assertEquals(createdReservation.getReservationDate(), reservation.getReservationDate());
        assertEquals(createdReservation.getPartySize(), reservation.getPartySize());
        assertEquals(createdReservation.getCustomerId(), reservation.getCustomerId());
        assertEquals(createdReservation.getCustomerName(), reservation.getCustomerName());
        assertEquals(createdReservation.getTableId(), reservation.getTableId());
    }

    @Test
    public void shouldUpdateReservation() {
        RestaurantReservation[] reservationsBefore =
                get(createURLWithPort("/api/reservations?restaurant=1"))
                        .then()
                        .statusCode(200)
                        .extract().as(RestaurantReservation[].class);

        String requestBody = "{\n" +
                "  \"partySize\": \"4\",\n" +
                "  \"tableId\": \"2\" \n}";

        Long updatedReservation = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put(createURLWithPort("/api/reservations/3"))
                .then()
                .extract().as(Long.class);

        RestaurantReservation[] reservationsAfter =
                get(createURLWithPort("/api/reservations?restaurant=1"))
                        .then()
                        .statusCode(200)
                        .extract().as(RestaurantReservation[].class);

        assertEquals(2, getReservationById(reservationsBefore, 3L).getPartySize());
        assertEquals(3, getReservationById(reservationsBefore, 3L).getTableId());
        assertEquals(3, updatedReservation);
        assertEquals(4, getReservationById(reservationsAfter, 3L).getPartySize());
        assertEquals(2, getReservationById(reservationsAfter, 3L).getTableId());
    }

    @Test
    public void shouldDeleteReservation() {
        RestaurantReservation[] reservationsBefore =
                get(createURLWithPort("/api/reservations?restaurant=1"))
                        .then()
                        .statusCode(200)
                        .extract().as(RestaurantReservation[].class);

        Long deletedReservation =
                delete(createURLWithPort("/api/reservations/3"))
                        .then()
                        .statusCode(200)
                        .extract().as(Long.class);

        RestaurantReservation[] reservationsAfter =
                get(createURLWithPort("/api/reservations?restaurant=1"))
                        .then()
                        .statusCode(200)
                        .extract().as(RestaurantReservation[].class);

        assertEquals(3, reservationsBefore.length);
        assertEquals(3, deletedReservation);
        assertEquals(2, reservationsAfter.length);

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private RestaurantReservation getReservationById(RestaurantReservation[] reservationsBefore, Long reservationId) {
        return Arrays.stream(reservationsBefore)
                .filter(r -> r.getReservationId() == reservationId)
                .findFirst()
                .get();
    }

}