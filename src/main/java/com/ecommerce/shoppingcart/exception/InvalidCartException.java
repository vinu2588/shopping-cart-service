package com.ecommerce.shoppingcart.exception;

/**
 * Exception thrown when cart validation fails
 */
public class InvalidCartException extends RuntimeException {
    public InvalidCartException(String message) {
        super(message);
    }
}


