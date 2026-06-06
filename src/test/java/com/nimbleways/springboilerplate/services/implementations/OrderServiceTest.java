package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.EmptyOrderException;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.api.IProductService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@UnitTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private IProductService productService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void whenOrderFoundReturnsOrder() {
        Order order = new Order(1L, Set.of());
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.get(1L);

        assertEquals(order, result);
    }

    @Test
    void whenOrderNotFoundThrows() {
        Mockito.when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.get(99L));
    }

    @Test
    void whenProcessCallsHandleForEachProduct() {
        Product p1 = new Product(1L, 5, 10, ProductType.NORMAL, "USB Cable", null, null, null);
        Product p2 = new Product(2L, 5, 10, ProductType.NORMAL, "USB Dongle", null, null, null);
        Order order = new Order(1L, Set.of(p1, p2));

        orderService.process(order);

        Mockito.verify(productService).handle(p1);
        Mockito.verify(productService).handle(p2);
    }

    @Test
    void whenProcessEmptyOrderThrowsException() {
        Order order = new Order(1L, Set.of());

        assertThrows(EmptyOrderException.class, () -> orderService.process(order));

        Mockito.verifyNoInteractions(productService);
    }
}
