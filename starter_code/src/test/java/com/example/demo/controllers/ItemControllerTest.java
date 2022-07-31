package com.example.demo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.example.demo.controllers.TestUtils.injectValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemControllerTest extends TestBase {
    private ItemController itemController;

    @BeforeEach
    public void init() {
        itemController = new ItemController();
        injectValue(itemController, "itemRepository", itemRepository);
    }

    @Test
    void can_get_all_items() {
        withExistsItem(2);
        var response = itemController.getItems();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void can_get_exists_item_by_id() {
        withExistsItem(2);
        var response = itemController.getItemById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void can_not_get_not_exists_item_by_id() {
        withExistsItem(2);
        var response = itemController.getItemById(3L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void can_get_exists_item_by_name() {
        withExistsItem(2);
        var response = itemController.getItemsByName("item1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}