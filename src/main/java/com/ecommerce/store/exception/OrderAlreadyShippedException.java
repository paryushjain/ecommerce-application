package com.ecommerce.store.exception;

public class OrderAlreadyShippedException extends RuntimeException {
    public OrderAlreadyShippedException(String message) {
        super(message);
    }

    public OrderAlreadyShippedException(String message, Throwable cause) {
        super(message, cause);
    }
}