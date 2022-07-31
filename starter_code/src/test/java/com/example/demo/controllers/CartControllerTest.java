package com.example.demo.controllers;

import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.example.demo.controllers.TestUtils.injectValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CartControllerTest extends TestBase {
    private CartController cartController;

    @BeforeEach
    public void init() {
        cartController = new CartController();
        injectValue(cartController, "userRepository", userRepository);
        injectValue(cartController, "cartRepository", cartRepository);
        injectValue(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void can_add_item_to_cart_with_exists_user() {
        withExistsUser(1);
        withExistsItem(1);

        var request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("admin");
        var response = cartController.addToCart(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getItems().size());
        assertEquals(1, response.getBody().getUser().getId());
    }

    @Test
    public void can_not_add_item_to_cart_with_not_exists_user() {
        withExistsUser(1);

        var request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("admin");
        var response = cartController.addToCart(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void can_not_add_item_to_cart_with_not_exists_item() {
        withExistsItem(1);

        var request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("admin");
        var response = cartController.addToCart(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void can_remove_item_from_cart_with_exists_user() {
        withExistsUser(2);
        withExistsItem(1);

        var request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("admin");
        var response = cartController.removeFromcart(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
        assertEquals(1, response.getBody().getUser().getId());
    }

    @Test
    void can_not_remove_item_from_cart_with_not_exists_user() {
        withExistsItem(1);

        var request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("admin");
        var response = cartController.removeFromcart(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void can_not_remove_item_from_cart_with_empty_items() {
        withExistsUser(0);

        var request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("admin");
        var response = cartController.removeFromcart(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}