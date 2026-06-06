package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.EmptyOrderException;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.services.api.IOrderService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@UnitTest
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    private Set<Product> products;

    @BeforeEach
    void setUp() {
        products = Set.of(
                new Product(null, 15, 30, ProductType.NORMAL, "USB Cable", null, null, null),
                new Product(null, 10, 0, ProductType.NORMAL, "USB Dongle", null, null, null),
                new Product(null, 15, 30, ProductType.EXPIRABLE, "Butter", LocalDate.now().plusDays(26), null, null),
                new Product(null, 90, 6, ProductType.EXPIRABLE, "Milk", LocalDate.now().minusDays(2), null, null),
                new Product(null, 15, 30, ProductType.SEASONAL, "Watermelon", null, LocalDate.now().minusDays(2), LocalDate.now().plusDays(58)),
                new Product(null, 15, 30, ProductType.SEASONAL, "Grapes", null, LocalDate.now().plusDays(180), LocalDate.now().plusDays(240))
        );
    }

    @Test
    void processOrderReturns200WithOrderId() throws Exception {
        Order order = new Order(1L, products);
        Mockito.when(orderService.get(1L)).thenReturn(order);

        mockMvc.perform(post("/orders/{orderId}/processOrder", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        Mockito.verify(orderService).process(order);
    }

    @Test
    void processOrderReturns404WhenOrderNotFound() throws Exception {
        Mockito.when(orderService.get(99L)).thenThrow(new OrderNotFoundException());

        mockMvc.perform(post("/orders/{orderId}/processOrder", 99L)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void processOrderReturns400WhenOrderEmpty() throws Exception {
        Order order = new Order(1L, Set.of());
        Mockito.when(orderService.get(1L)).thenReturn(order);
        Mockito.doThrow(new EmptyOrderException()).when(orderService).process(order);

        mockMvc.perform(post("/orders/{orderId}/processOrder", 1L)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
