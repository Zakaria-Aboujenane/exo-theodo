package com.nimbleways.springboilerplate.handlers.product.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.implementations.NotificationService;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ExpirableProductHandlerTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private ExpirableProductHandler handler;

    @Test
    void whenAvailableAndNotExpiredDecrements() {
        Product product = new Product(1L, 15, 10, ProductType.EXPIRABLE, "Butter",
                LocalDate.now().plusDays(5), null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(9, product.getAvailable());
        Mockito.verify(productRepository).save(product);
        Mockito.verifyNoInteractions(notificationService);
    }

    @Test
    void whenExpiredSendsExpirationNotification() {
        LocalDate expiry = LocalDate.now().minusDays(1);
        Product product = new Product(1L, 15, 10, ProductType.EXPIRABLE, "Milk", expiry, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendExpirationNotification("Milk", expiry);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenOutOfStockSendsExpirationNotification() {
        LocalDate expiry = LocalDate.now().plusDays(5);
        Product product = new Product(1L, 15, 0, ProductType.EXPIRABLE, "Butter", expiry, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendExpirationNotification("Butter", expiry);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenExpiresTodayTreatedAsExpired() {
        LocalDate expiry = LocalDate.now();
        Product product = new Product(1L, 15, 10, ProductType.EXPIRABLE, "Butter", expiry, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendExpirationNotification("Butter", expiry);
        Mockito.verify(productRepository).save(product);
    }
}
