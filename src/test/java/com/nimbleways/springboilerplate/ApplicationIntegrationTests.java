package com.nimbleways.springboilerplate;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private NotificationService notificationService;

    @Test
    void processOrderReturns200() {
        List<Product> saved = productRepository.saveAll(List.of(
                new Product(null, 15, 30, ProductType.NORMAL, "USB Cable", null, null, null)
//                new Product(null, 10, 0, ProductType.NORMAL, "USB Dongle", null, null, null),
//                new Product(null, 15, 30, ProductType.EXPIRABLE, "Butter", LocalDate.now().plusDays(26), null, null),
//                new Product(null, 90, 6, ProductType.EXPIRABLE, "Milk", LocalDate.now().minusDays(2), null, null),
//                new Product(null, 15, 30, ProductType.SEASONAL, "Watermelon", null, LocalDate.now().minusDays(2), LocalDate.now().plusDays(58)),
//                new Product(null, 15, 30, ProductType.SEASONAL, "Grapes", null, LocalDate.now().plusDays(180), LocalDate.now().plusDays(240))
        ));
        Order order = orderRepository.save(new Order(null, Set.copyOf(saved)));

        ResponseEntity<ProcessOrderResponse> response = restTemplate.postForEntity(
                "/api/orders/{orderId}/processOrder",
                null,
                ProcessOrderResponse.class,
                order.getId()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(order.getId(), response.getBody().id());
    }

    @Test
    void processOrderReturns404WhenOrderNotFound() {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/orders/{orderId}/processOrder",
                null,
                Void.class,
                999L
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
