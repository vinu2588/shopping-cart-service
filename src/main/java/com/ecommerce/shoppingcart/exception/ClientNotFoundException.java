package com.ecommerce.shoppingcart.exception;

/**
 * Exception thrown when a client is not found
 * Following fail-fast principle
 */
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}