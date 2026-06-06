package com.nimbleways.springboilerplate.handlers.product.api;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;

public interface ProductHandler {
    ProductType getSupportedType();
    void handle(Product p);
}
