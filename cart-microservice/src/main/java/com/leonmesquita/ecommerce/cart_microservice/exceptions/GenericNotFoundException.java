package com.leonmesquita.ecommerce.cart_microservice.exceptions;

public class GenericNotFoundException extends RuntimeException {
    public GenericNotFoundException(String message) {
        super(message);
    }
}
