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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@UnitTest
class NormalProductHandlerTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private NormalProductHandler handler;

    @Test
    void whenAvailableDecrementsAndSaves() {
        Product product = new Product(1L, 15, 10, ProductType.NORMAL, "USB Cable", null, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(9, product.getAvailable());
        Mockito.verify(productRepository).save(product);
        Mockito.verifyNoInteractions(notificationService);
    }

    @Test
    void whenOutOfStockSendsDelayNotification() {
        Product product = new Product(1L, 15, 0, ProductType.NORMAL, "USB Dongle", null, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        Mockito.verify(notificationService).sendDelayNotification(15, "USB Dongle");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenZeroLeadTimeNoNotificationSaves() {
        Product product = new Product(1L, 0, 0, ProductType.NORMAL, "USB Cable", null, null, null);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        Mockito.verify(productRepository).save(product);
        Mockito.verifyNoInteractions(notificationService);
    }
}
