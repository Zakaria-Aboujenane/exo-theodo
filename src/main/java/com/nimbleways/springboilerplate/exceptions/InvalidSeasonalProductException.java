package com.nimbleways.springboilerplate.exceptions;

public class InvalidSeasonalProductException extends RuntimeException {
    public InvalidSeasonalProductException() {
        super("Seasonal product missing season dates");
    }
}
