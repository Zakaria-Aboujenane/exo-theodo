package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.NoProductHandlerException;
import com.nimbleways.springboilerplate.handlers.product.api.ProductHandler;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@UnitTest
class ProductServiceTest {

    @Mock
    private ProductHandler normalHandler;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        Mockito.when(normalHandler.getSupportedType()).thenReturn(ProductType.NORMAL);
        productService = new ProductService(List.of(normalHandler));
    }

    @Test
    void whenHandlerExistsDelegatesToHandler() {
        Product product = new Product(1L, 5, 10, ProductType.NORMAL, "USB Cable", null, null, null);

        productService.handle(product);

        Mockito.verify(normalHandler).handle(product);
    }

    @Test
    void whenNoHandlerThrows() {
        Product product = new Product(1L, 5, 10, ProductType.SEASONAL, "Watermelon", null, null, null);

        assertThrows(NoProductHandlerException.class, () -> productService.handle(product));
    }
}
