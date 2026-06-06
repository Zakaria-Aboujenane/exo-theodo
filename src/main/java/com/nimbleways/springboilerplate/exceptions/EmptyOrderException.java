package com.nimbleways.springboilerplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException() {
        super("This order has no products, nothing to process");
    }
}
