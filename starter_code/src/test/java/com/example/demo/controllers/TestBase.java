package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class TestBase {
    protected UserRepository userRepository = mock(UserRepository.class);
    protected CartRepository cartRepository = mock(CartRepository.class);
    protected ItemRepository itemRepository = mock(ItemRepository.class);
    protected OrderRepository orderRepository = mock(OrderRepository.class);

    protected void withExistsOrder(int orderCount) {
        var orders = new ArrayList<UserOrder>(orderCount);
        for (var i = 0; i < orderCount; i++) {
            var order = new UserOrder();
            order.setTotal(BigDecimal.valueOf(100));
            orders.add(order);
        }

        var user = new User();
        user.setId(1);
        when(orderRepository.findByUser(user)).thenReturn(orders);
    }

    protected void withExistsItem(int itemCount) {
        var items = createItems(itemCount);
        for (var item : items) {
            when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
            when(itemRepository.findByName(item.getName())).thenReturn(List.of(item));
        }

        when(itemRepository.findAll()).thenReturn(items);
    }

    protected void withExistsUser(int itemCount) {
        var user = new User();
        user.setId(1);
        user.setUsername("admin");
        var cart = new Cart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.valueOf(0));
        user.setCart(cart);
        cart.setUser(user);
        for (Item item : createItems(itemCount)) {
            cart.addItem(item);
        }

        when(userRepository.findByUsername("admin")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    }

    private List<Item> createItems(int itemCount) {
        var items = new ArrayList<Item>(itemCount);
        for (var i = 0; i < itemCount; i++) {
            var item = new Item();
            item.setId(i + 1L);
            item.setName("item" + item.getId());
            item.setPrice(BigDecimal.valueOf(100));
            items.add(item);
        }
        return items;
    }
}
