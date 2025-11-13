package com.ecommerce.shoppingcart.exception;

/**
 * Exception thrown when attempting to create a client with an existing client ID
 */
public class DuplicateClientException extends RuntimeException {
    public DuplicateClientException(String message) {
        super(message);
    }
}


