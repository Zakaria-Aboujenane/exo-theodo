package com.nimbleways.springboilerplate.exceptions;

import com.nimbleways.springboilerplate.enums.ProductType;

public class NoProductHandlerException extends RuntimeException {
    public NoProductHandlerException(ProductType type) {
        super("No handler for product type: " + type);
    }
}
