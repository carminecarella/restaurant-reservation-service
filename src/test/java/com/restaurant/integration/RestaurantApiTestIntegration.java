package com.restaurant.integration;

import com.restaurant.domain.Restaurant;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext
class RestaurantApiTestIntegration {

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setup() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @Order(1)
    public void shouldReturnAllRestaurants() {
        Restaurant[] restaurants =
                get(createURLWithPort("/api/restaurants"))
               .then()
               .statusCode(200)
               .extract().as(Restaurant[].class);

        assertEquals(2, restaurants.length);
        assertEquals("Tacos", restaurants[0].getName());
        assertEquals("London", restaurants[0].getLocation());
    }

    @Test
    @Order(2)
    public void shouldReturnRestaurantById() {
        Restaurant restaurant =
                get(createURLWithPort("/api/restaurants/1"))
                        .then()
                        .statusCode(200)
                        .extract().as(Restaurant.class);

        assertEquals("Tacos", restaurant.getName());
        assertEquals("London", restaurant.getLocation());
    }

    @Test
    @Order(3)
    public void shouldReturn404WhenRestaurantByIdIsNotFound() {
        get(createURLWithPort("/api/restaurants/5"))
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    public void shouldCreateARestaurant() {
        String requestBody = "{\n" +
                "    \"restaurantName\": \"Pizzerium\",\n" +
                "    \"location\": \"Epsom\",\n" +
                "    \"tableCapacities\": [\n" +
                "        2,\n" +
                "        3,\n" +
                "        4\n" +
                "    ]\n" +
                "}";

        Restaurant createdRestaurant = given()
                .auth().basic("admin", "admin123")
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(createURLWithPort("/api/restaurants"))
                .then()
                .extract().as(Restaurant.class);

        Restaurant restaurant =
                get(createURLWithPort(String.format("/api/restaurants/%s", createdRestaurant.getId())))
                        .then()
                        .statusCode(200)
                        .extract().as(Restaurant.class);

        assertEquals(createdRestaurant.getId(), restaurant.getId());
        assertEquals(createdRestaurant.getName(), restaurant.getName());
        assertEquals(createdRestaurant.getLocation(), restaurant.getLocation());
    }

    @Test
    @Order(5)
    public void shouldReturn400WhenCreatingARestaurantWithSameNameInLocation() {
        String requestBody = "{\n" +
                "    \"restaurantName\": \"Thai\",\n" +
                "    \"location\": \"Kingston\",\n" +
                "    \"tableCapacities\": [\n" +
                "        2,\n" +
                "        3,\n" +
                "        4\n" +
                "    ]\n" +
                "}";

        Restaurant createdRestaurant = given()
                .auth().basic("admin", "admin123")
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(createURLWithPort("/api/restaurants"))
                .then()
                .extract().as(Restaurant.class);

        given()
                .auth().basic("admin", "admin123")
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(createURLWithPort("/api/restaurants"))
                .then()
                .assertThat().statusCode(400);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}