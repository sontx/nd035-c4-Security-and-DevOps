package com.example.demo.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.example.demo.controllers.TestUtils.injectValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest extends TestBase {
    private OrderController orderController;

    @BeforeEach
    public void init() {
        orderController = new OrderController();
        injectValue(orderController, "userRepository", userRepository);
        injectValue(orderController, "orderRepository", orderRepository);
    }

    @Test
    void can_submit_order_with_exists_user() {
        withExistsUser(2);
        var response = orderController.submit("admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getItems().size());
        assertThat(response.getBody().getTotal(), Matchers.comparesEqualTo(BigDecimal.valueOf(200)));
        assertEquals("admin", response.getBody().getUser().getUsername());
    }

    @Test
    void can_not_submit_order_with_not_exists_user() {
        var response = orderController.submit("not_exist_user");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void can_get_order_history_with_exists_user() {
        withExistsUser(2);
        withExistsOrder(2);

        var response = orderController.getOrdersForUser("admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void can_not_get_order_history_with_not_exists_user() {
        var response = orderController.getOrdersForUser("not_exist_user");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}