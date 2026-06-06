package com.nimbleways.springboilerplate.repositories;

import com.nimbleways.springboilerplate.entities.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByIdReturnsOrderWhenExists() {
        Order saved = orderRepository.save(new Order(null, Set.of()));

        Optional<Order> result = orderRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    void findByIdReturnsEmptyWhenNotExists() {
        Optional<Order> result = orderRepository.findById(999L);

        assertTrue(result.isEmpty());
    }
}
