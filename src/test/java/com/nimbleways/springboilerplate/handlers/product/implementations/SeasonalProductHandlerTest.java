package com.nimbleways.springboilerplate.handlers.product.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.InvalidSeasonalProductException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@UnitTest
class SeasonalProductHandlerTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private SeasonalProductHandler handler;

    @Test
    void whenInSeasonDecrementsAndSaves() {
        Product product = new Product(1L, 5, 10, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(10));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(9, product.getAvailable());
        Mockito.verify(productRepository).save(product);
        Mockito.verifyNoInteractions(notificationService);
    }

    @Test
    void whenOnStartDateTreatedAsInSeason() {
        Product product = new Product(1L, 5, 10, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now(), LocalDate.now().plusDays(30));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(9, product.getAvailable());
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenOnEndDateTreatedAsInSeason() {
        Product product = new Product(1L, 5, 10, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now().minusDays(30), LocalDate.now());
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(9, product.getAvailable());
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenLeadTimeExceedsSeasonSendsOutOfStock() {
        Product product = new Product(1L, 10, 0, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(5));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendOutOfStockNotification("Watermelon");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenLeadTimeFitsSeasonSendsDelay() {
        Product product = new Product(1L, 5, 0, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now().minusDays(10), LocalDate.now().plusDays(20));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        Mockito.verify(notificationService).sendDelayNotification(5, "Watermelon");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenBeforeSeasonLeadTimeExceedsSendsOutOfStock() {
        Product product = new Product(1L, 15, 30, ProductType.SEASONAL, "Grapes", null,
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(20));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendOutOfStockNotification("Grapes");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenBeforeSeasonLeadTimeFitsSendsDelay() {
        Product product = new Product(1L, 10, 30, ProductType.SEASONAL, "Grapes", null,
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(30));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        Mockito.verify(notificationService).sendDelayNotification(10, "Grapes");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenAfterSeasonSendsOutOfStock() {
        Product product = new Product(1L, 5, 10, ProductType.SEASONAL, "Watermelon", null,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(1));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        handler.handle(product);

        assertEquals(0, product.getAvailable());
        Mockito.verify(notificationService).sendOutOfStockNotification("Watermelon");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void whenNullDatesThrows() {
        Product product = new Product(1L, 5, 10, ProductType.SEASONAL, "Watermelon", null, null, null);

        assertThrows(InvalidSeasonalProductException.class, () -> handler.handle(product));
    }
}
